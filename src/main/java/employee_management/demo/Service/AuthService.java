package employee_management.demo.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import employee_management.demo.DTO.*;
import employee_management.demo.Mapper.UserMapper;
import jakarta.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import employee_management.demo.Modal.AuthProvider;
import employee_management.demo.Modal.Role;
import employee_management.demo.Modal.User;
import employee_management.demo.AuthRepository.RoleRepository;
import employee_management.demo.AuthRepository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RoleRepository roleRepository, EmailService emailService,
            RefreshTokenService refreshTokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
    }

    // =========================
    // REGISTER
    // =========================
    @Transactional(transactionManager ="authTransactionManager")
    public APIResponse<?> registerUser(RegisterRequest dto) throws MessagingException {

        String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);
        String username = dto.getUsername().trim();

        // Check email already exists
        if (userRepository.findByEmail(email).isPresent()) {

            return new APIResponse<>(
                    "Email already exists",
                    409,
                    "CONFLICT",
                    null,
                    null);
        }

        if (userRepository.existsByUsername(username)) {
            return new APIResponse<>(
                    "Username already exists", 409, "CONFLICT", null, null);
        }

        // Create user
        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Local registration
        user.setAuthProvider(AuthProvider.LOCAL);
        // There is no verification endpoint yet, so local accounts must be
        String emailVerificationToken = jwtService.generateToken(email, "EMAIL_VERIFICATION");
        String verificationLink =
                "http://localhost:8081/api/v1/auth/verify-email?token="
                        + emailVerificationToken;
        // usable immediately after registration.

//        user.setEmailVerified(true);
//        user.setEmailVerifiedAt(java.time.LocalDateTime.now());

        // Default role
        Role role = roleRepository.findByRoleName("ROLE_EMPLOYEE")
                .orElseGet(() -> {
                    Role defaultRole = new Role();
                    defaultRole.setRoleName("ROLE_EMPLOYEE");
                    return roleRepository.save(defaultRole);
                });

        user.getRoles().add(role);

        // Save user
        userRepository.save(user);
        emailService.sendEmail(
                user.getEmail(),
                "Please verify your email",
                "Please click the verification link to verify your email." + verificationLink,
                List.of()
        );

        return new APIResponse<>(
                "User created successfully",
                201,
                "CREATED",
                null,
                null);
    }

    // =========================
    // LOGIN
    // =========================

    public APIResponse<LoginResponse> loginService(LoginRequest dto) {

        // Find user
        String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return new APIResponse<>(
                    "Invalid email or password", 401, "INVALID_CREDENTIALS", null, null);
        }

        // Check account status
        if (!user.isActive()) {

            return new APIResponse<>(
                    "User account is inactive",
                    403,
                    "ACCOUNT_INACTIVE",
                    null,
                    null);
        }

        // Check email verification
        if (!user.isEmailVerified()) {

            return new APIResponse<>(
                    "Email is not verified",
                    403,
                    "EMAIL_NOT_VERIFIED",
                    null,
                    null);
        }

        // Generate JWT
        String token = jwtService.generateToken(
                user.getEmail(),
                "LOGIN"
        );

        LoginResponse loginResponse = userMapper.toLoginResponse(
                user, token, refreshTokenService.issue(user));

        // Return response
        return new APIResponse<>(
                "Login successful",
                200,
                "SUCCESS",
                null,
                loginResponse);
    }

    @Transactional(transactionManager = "authTransactionManager")
    public APIResponse<LoginResponse> refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.rotate(request.refreshToken())
                .map(rotation -> {
                    User user = rotation.user();
                    LoginResponse response = userMapper.toLoginResponse(
                            user,
                            jwtService.generateToken(user.getEmail(), "LOGIN"),
                            rotation.refreshToken());
                    return new APIResponse<LoginResponse>("Token refreshed", 200,
                            "SUCCESS", null, response);
                })
                .orElseGet(() -> new APIResponse<>("Invalid or expired refresh token", 401,
                        "INVALID_REFRESH_TOKEN", null, null));
    }

    //verify user email using token
    public APIResponse<?> verifyUserEmail(String token) {

        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (RuntimeException exception) {
            return new APIResponse<>(
                    "Invalid or expired verification token",
                    401,
                    "INVALID_TOKEN",
                    null,
                    null
            );
        }
        if (!jwtService.validateToken(
                token,
                email,
                "EMAIL_VERIFICATION"
        )) {
            return new APIResponse<>(
                    "Invalid or expired verification token",
                    401,
                    "INVALID_TOKEN",
                    null,
                    null
            );
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new APIResponse<>(
                    "no user found",
                    404,
                    "NOT_FOUND",
                    null,
                    null
            );
        }
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
        return new APIResponse<>(
                "user email verified successfully",
                200,
                "SUCCESS",
                null,
                null
        );
    }

    public APIResponse<?> forgotPassword(String email) throws MessagingException {

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(normalizedEmail).orElse(null);

        if (user == null) {
            return new APIResponse<>(
                    "If the account exists, a password reset link will be sent",
                    200,
                    "SUCCESS",
                    null,
                    null
            );
        }

        String passwordResetToken =
                jwtService.generateToken(normalizedEmail, "FORGOT_PASSWORD");

        String passwordResetLink =
                "http://localhost:8081/reset-password?token=" + passwordResetToken;

        emailService.sendEmail(
                user.getEmail(),
                "Reset your password",
                "Please use this link to reset your password: "
                        + passwordResetLink,
                List.of()
        );

        return new APIResponse<>(
                "Password reset link sent to your email",
                200,
                "SUCCESS",
                null,
                null
        );
    }

    public APIResponse<?> resetPassword(ResetPasswordRequest dto) {

        String email;
        try {
            email = jwtService.extractEmail(dto.getToken());
        } catch (RuntimeException exception) {
            return new APIResponse<>("Invalid or expired reset token", 401,
                    "INVALID_TOKEN", null, null);
        }

        if (!jwtService.validateToken(dto.getToken(), email, "FORGOT_PASSWORD")) {
            return new APIResponse<>("Invalid or expired reset token", 401,
                    "INVALID_TOKEN", null, null);
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return new APIResponse<>(
                    "No user found",
                    404,
                    "NOT_FOUND",
                    null,
                    null
            );
        }

        String encodedPassword =
                passwordEncoder.encode(dto.getNewPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return new APIResponse<>(
                "Password reset successful",
                200,
                "SUCCESS",
                null,
                null
        );
    }
}
