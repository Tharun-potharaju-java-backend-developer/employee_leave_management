package employee_management.demo.EmployeeRepository;

import employee_management.demo.Modal.LeaveTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveTypes, Integer> {
}
