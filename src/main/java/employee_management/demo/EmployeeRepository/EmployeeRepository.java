package employee_management.demo.EmployeeRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import employee_management.demo.Modal.Employees;

public interface EmployeeRepository extends JpaRepository<Employees, String> {

    boolean existsByUserId(String userId);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByPhoneNumber(String phoneNumber);
}
