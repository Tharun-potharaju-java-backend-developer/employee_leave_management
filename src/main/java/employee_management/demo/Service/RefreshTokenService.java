package employee_management.demo.Service;

import employee_management.demo.Modal.User;
import employee_management.demo.AuthRepository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Handles refresh JWTs without persisting token state.
 */
@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public RefreshTokenService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String issue(User user) {
        return jwtService.generateRefreshToken(user.getEmail());
    }

    public Optional<Rotation> rotate(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return Optional.empty();
        }

        final String email;
        try {
            email = jwtService.extractRefreshEmail(rawToken);
        } catch (RuntimeException exception) {
            return Optional.empty();
        }

        if (!jwtService.validateToken(rawToken, email, "REFRESH")) {
            return Optional.empty();
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !user.isActive()) {
            return Optional.empty();
        }

        // Rotation issues a new self-contained token; the old token remains
        // valid until its expiry because no server-side revocation state exists.
        return Optional.of(new Rotation(user, issue(user)));
    }

    public record Rotation(User user, String refreshToken) {
    }
}
