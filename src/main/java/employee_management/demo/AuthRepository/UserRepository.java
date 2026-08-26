package employee_management.demo.AuthRepository;

import employee_management.demo.Modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findById(String id);
    boolean existsByUsername(String username);
}
