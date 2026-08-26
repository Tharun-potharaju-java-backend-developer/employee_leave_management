package employee_management.demo.DTO;

import employee_management.demo.Modal.Holiday;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetHoliday {
    @NotBlank
    private String holidayName;
    @NotBlank
    private LocalDate holidayDate;
    @NotBlank
    private String description;
}
