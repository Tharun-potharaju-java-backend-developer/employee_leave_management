package employee_management.demo.Service;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.AddEmployee;
import employee_management.demo.DTO.GetEmployee;
import employee_management.demo.Exception.ResourceNotFoundException;
import employee_management.demo.Modal.Employees;
import employee_management.demo.EmployeeRepository.EmployeeRepository;
import employee_management.demo.AuthRepository.UserRepository;
import employee_management.demo.Mapper.EmployeeMapper;
import employee_management.demo.Modal.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            EmployeeMapper employeeMapper) {

        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<?> addEmployees(AddEmployee dto) {

        userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        if (employeeRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("An employee already exists for this user");
        }
        if (employeeRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code is already in use");
        }
        if (employeeRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is already in use");
        }
        Employees employee = employeeMapper.toEntity(dto);


        return new APIResponse<>("Employee created successfully", HttpStatus.CREATED.value(), "CREATED", null, employeeRepository.save(employee));
    }

    @Transactional(transactionManager = "employeeTransactionManager", readOnly = true)
    public APIResponse<List<GetEmployee>> getEmployeesService() {

        List<Employees> employees = employeeRepository.findAll();


        List<GetEmployee> employeeList = employees.stream().map(emp -> {

            return employeeMapper.toGetEmployee(
                    emp,
                    userRepository.findById(emp.getUserId()).orElse(null));

        }).toList();

        return new APIResponse<>("List of employees fetched successfully", 200, "SUCCESS", null, employeeList);
    }

    // update employee
    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<GetEmployee> editEmployeeService(String empId, AddEmployee dto) {
        Employees employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));

        if (!employee.getUserId().equals(dto.getUserId())
                && employeeRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("An employee already exists for this user");
        }
        if (!employee.getEmployeeCode().equals(dto.getEmployeeCode())
                && employeeRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code is already in use");
        }
        if (!employee.getPhoneNumber().equals(dto.getPhoneNumber())
                && employeeRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is already in use");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        employeeMapper.updateEmployee(employee, dto);

        Employees savedEmployee = employeeRepository.save(employee);
        GetEmployee response = employeeMapper.toGetEmployee(
                savedEmployee,
                user);

        return new APIResponse<>("Employee updated successfully", HttpStatus.OK.value(), "UPDATED", null, response);

    }

    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<?> deleteEmployeeService(String empId) {
        employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));
        employeeRepository.deleteById(empId);
        return new APIResponse<>(
                "Employee deleted successfully", HttpStatus.OK.value(), "SUCCESS", null, empId
        );

    }

}
