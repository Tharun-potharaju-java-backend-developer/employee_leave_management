package employee_management.demo.Controller;

import employee_management.demo.DTO.*;
import employee_management.demo.Service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public APIResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest dto
    ) {
        return authService.loginService(dto);

    }

    @PostMapping("/refresh")
    public APIResponse<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }


    @PostMapping("/register")
    public APIResponse<?> register(@Valid @RequestBody RegisterRequest dto) throws MessagingException {
        return authService.registerUser(dto);

    }
    @PostMapping("/forgot-password")
    public APIResponse<?> forgotPassword(@RequestParam String email) throws MessagingException {
        return authService.forgotPassword(email);

    }
    @PostMapping("/reset-password")
    public APIResponse<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest dto
            ){
        return authService.resetPassword(dto);

    }


    @GetMapping("/verify-email")
    public APIResponse<?> verifyUserEmail(
            @RequestParam String token
    ) {
        return authService.verifyUserEmail(token);
    }

}
