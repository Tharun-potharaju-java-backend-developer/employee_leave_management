package employee_management.demo.EmployeeRepository;

import employee_management.demo.Modal.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository  extends JpaRepository<Holiday,String> {
}
