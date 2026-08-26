package employee_management.demo.Mapper;

import employee_management.demo.DTO.AddEmployee;
import employee_management.demo.DTO.GetEmployee;
import employee_management.demo.Modal.Employees;
import employee_management.demo.Modal.User;
import org.springframework.stereotype.Component;

/**
 * Converts employee domain objects into API response DTOs.
 * The mapper handles presentation transformation only; it does not query
 * repositories or apply business rules.
 */
@Component
public class EmployeeMapper {

    public GetEmployee toGetEmployee(Employees employee, User user) {
        GetEmployee dto = new GetEmployee();
        dto.setId(employee.getId());

        if (user != null && user.isEmailVerified() && user.isActive()) {
            dto.setUsername(user.getUsername());
        }

        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setPhone(employee.getPhoneNumber());
        dto.setDesignation(employee.getDesignation());
        dto.setJoiningDate(employee.getJoiningDate());
        return dto;
    }

    public void updateEmployee(Employees employee, AddEmployee dto) {
        employee.setId(employee.getId());
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDesignation(dto.getDesignation());
        employee.setJoiningDate(dto.getJoiningDate());
    }
    public  Employees toEntity(AddEmployee dto) {
        Employees employee = new Employees();
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setDesignation(dto.getDesignation());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setJoiningDate(dto.getJoiningDate());
        return employee;
    }
}
