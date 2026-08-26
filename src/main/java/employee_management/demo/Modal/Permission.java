package employee_management.demo.Modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    private int id;
    @Column(nullable = false,name = "permission_name")
    private String permissionName;
    @Column(name = "is_active",nullable = false)
    private boolean isActive=true;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}
