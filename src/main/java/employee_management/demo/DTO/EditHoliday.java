package employee_management.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EditHoliday {
    private String holidayId;
    private String holidayName;
    private String holidayDescription;
    private String holidayDate;
}
