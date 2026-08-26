package employee_management.demo.Controller;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.AddEmployee;
import employee_management.demo.DTO.GetEmployee;
import employee_management.demo.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public APIResponse<?> addEmployee(@Valid @RequestBody AddEmployee dto) {
        return employeeService.addEmployees(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<List<GetEmployee>> getEmployees() {
        return employeeService.getEmployeesService();
    }

    @PatchMapping("/{empId}")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<GetEmployee> editEmployee(
            @PathVariable String empId,
            @Valid @RequestBody AddEmployee dto) {

        return employeeService.editEmployeeService(empId, dto);
    }
    @DeleteMapping("/{empId}")
    @PreAuthorize("hasAdmin('ADMIN')")
    public APIResponse<?> deleteEmployee(@PathVariable  String empId){
        return employeeService.deleteEmployeeService(empId);

    }

}
