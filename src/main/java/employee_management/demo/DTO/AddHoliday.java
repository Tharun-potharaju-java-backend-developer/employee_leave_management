package employee_management.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AddHoliday {
    @NotBlank
    private String holidayName;
    @NotBlank
    private String holidayDate;
    @NotBlank
    private String holidayDescription;
}
