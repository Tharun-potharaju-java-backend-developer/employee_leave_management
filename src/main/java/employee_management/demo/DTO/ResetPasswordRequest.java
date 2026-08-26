package employee_management.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResetPasswordRequest {
    @NotBlank
    private String token;
    @NotBlank
    @Size(min = 8, max = 100)
    private String newPassword;
}
