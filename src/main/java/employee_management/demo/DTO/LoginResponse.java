package employee_management.demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginResponse {
    private String token;
    private String refreshToken;
    private String username;
    private String id;
    private Set<String> roles;
    private Set<String> permissions;

}
