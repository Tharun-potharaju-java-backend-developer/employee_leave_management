package employee_management.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 2, max = 100)
    private String username;
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
