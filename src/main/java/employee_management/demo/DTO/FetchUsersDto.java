package employee_management.demo.DTO;

import employee_management.demo.Modal.AuthProvider;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchUsersDto {
    private String id;
    private String username;
    private String email;
    private boolean isEmailVerified;
    private AuthProvider authProvider;
    private boolean isActive;
    private List<String> roles;
    private List<String> permissions;
}
