package employee_management.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AddEmployee {
    @NotBlank
    private String userId;
    @NotBlank
    @Size(min = 1, max = 10)
    private String employeeCode;
    @NotBlank
    @Pattern(regexp = "\\d{10}")
    private String phoneNumber;
    @NotBlank
    private String designation;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate joiningDate;
}
