package employee_management.demo.Modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Getter
@Table(name = "employees")
public class Employees {
    @Id
    private String id;
    @Column(name = "user_id" ,nullable = false,unique = true)
    private String userId;
    @Column(name = "employee_code",nullable = false,unique = true)
    private String employeeCode;
    @Column(name = "phone",nullable = false,unique = true)
    private String phoneNumber;
    @Column(name = "designation",nullable=false)
    private String designation;
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;
    @CurrentTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @PrePersist
    public void generateId()
    {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
