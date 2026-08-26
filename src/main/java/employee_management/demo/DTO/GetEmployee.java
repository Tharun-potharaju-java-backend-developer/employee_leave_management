package employee_management.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetEmployee {
    private String Id;
    private String username;
    private String employeeCode;
    private String phone;
    private String designation;
    private LocalDate joiningDate;

}
