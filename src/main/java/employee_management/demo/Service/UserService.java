package employee_management.demo.Service;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.FetchUsersDto;
import employee_management.demo.Exception.ResourceNotFoundException;
import employee_management.demo.Modal.User;
import employee_management.demo.AuthRepository.UserRepository;
import employee_management.demo.Mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/*
 * @Service tells Spring that this class contains business logic.
 *
 * During application startup, Spring scans the project for classes
 * annotated with @Service and creates exactly one object (bean)
 * of this class inside the Spring IoC (Inversion of Control) Container.
 *
 * Because Spring manages this object, it can automatically inject
 * dependencies like AuthRepository whenever this service is needed.
 *
 * Instead of writing:
 *
 *      AuthService authService = new AuthService(...);
 *
 * Spring creates and manages the object for us.
 */
public class UserService {

    /*
     * AuthRepository is responsible for communicating with the database.
     *
     * This service does NOT write SQL queries.
     * Instead, it delegates database operations to the repository.
     *
     * Repository Layer
     *        ↓
     * authRepository.findAll()
     *        ↓
     * Hibernate
     *        ↓
     * SQL Query
     *        ↓
     * Database
     *
     * 'final' means this reference can only be assigned once.
     * After constructor injection, it cannot point to another
     * AuthRepository object.
     *
     * This prevents accidental reassignment and makes the dependency
     * mandatory.
     */
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /*
     * Constructor Dependency Injection.
     *
     * Spring sees that AuthService requires AuthRepository.
     *
     * Since AuthRepository is already a Spring Bean,
     * Spring automatically creates it and passes it here.
     *
     * We never manually create the repository using:
     *
     *      new AuthRepository();
     *
     * Spring performs the injection automatically.
     *
     * Flow:
     *
     * Spring Boot Starts
     *          ↓
     * Creates AuthRepository Bean
     *          ↓
     * Creates AuthService Bean
     *          ↓
     * Injects AuthRepository into AuthService
     */
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /*
     * Fetch all users from the database.
     *
     * Notice that this method DOES NOT return User entities directly.
     *
     * Instead, it returns FetchUsersDto objects.
     *
     * Why?
     *
     * Entity objects usually contain every column from the database.
     *
     * Example:
     *
     * User
     * ------------------------
     * id
     * username
     * email
     * password
     * createdAt
     * updatedAt
     * roles
     * permissions
     *
     * The client does not need all of this data.
     *
     * Returning entities directly:
     *
     * ❌ Exposes unnecessary information.
     * ❌ Can expose sensitive fields like passwords.
     * ❌ Couples API responses to database structure.
     *
     * Instead, DTOs expose only the required fields.
     *
     * Example DTO:
     *
     * id
     * username
     * email
     *
     * Nothing else.
     *
     * This improves:
     *
     * • Security
     * • Performance
     * • Maintainability
     * • Clean API Design
     */
    @Transactional(transactionManager = "authTransactionManager", readOnly = true)
    public APIResponse<List<FetchUsersDto>> fetchUsers() {

        /*
         * Repository's findAll() method is inherited from Jpa Repository.
         *
         * We did not implement it ourselves.
         *
         * JpaRepository already provides methods like:
         *
         * save()
         * delete()
         * findById()
         * existsById()
         * findAll()
         *
         * Internally Hibernate generates SQL similar to:
         *
         * SELECT * FROM users;
         *
         * Hibernate executes the SQL,
         * reads every row,
         * converts each row into a User object,
         * and finally returns:
         *
         * List<User>
         */
        List<User> users = userRepository.findAll();


        /*
         * Now we convert List<User>
         * into
         * List<FetchUsersDto>.
         *
         * Stream API allows processing collections in a functional way.
         *
         * Instead of writing loops manually,
         * streams perform operations like:
         *
         * filter()
         * map()
         * sorted()
         * distinct()
         * collect()
         *
         * Here we use map().
         *
         * map() transforms one object into another.
         *
         * User
         *      ↓
         * FetchUsersDto
         *
         * Every User becomes one DTO.
         */
        List<FetchUsersDto> usersDto = users.stream()

                /*
                 * user represents one User object
                 * from the users list.
                 *
                 * Imagine:
                 *
                 * users =
                 *
                 * User1
                 * User2
                 * User3
                 *
                 * map() runs three times.
                 *
                 * First iteration:
                 * user = User1
                 *
                 * Second iteration:
                 * user = User2
                 *
                 * Third iteration:
                 * user = User3
                 */
                .map(user -> {

                    /*
                     * Create a brand-new DTO object.
                     *
                     * DTO objects are independent of Entity objects.
                     *
                     * They only contain data that will be sent
                     * to the client.
                     */
                    return userMapper.toFetchUsersDto(user);
                })

                /*
                 * Convert the Stream<FetchUsersDto>
                 * into List<FetchUsersDto>.
                 *
                 * Without toList(),
                 * the result would remain a Stream.
                 *
                 * Final output:
                 *
                 * List<FetchUsersDto>
                 *
                 * This list is returned to the controller,
                 * which then converts it into JSON and sends it
                 * back to the client.
                 */
                .toList();
        return new APIResponse<>(
                "Users fetched successfully",
                200,
                "SUCCESS",
                null,
                usersDto
        );
    }

    @Transactional(transactionManager = "authTransactionManager")
    public APIResponse<?> deleteById(String id) {
        // check user exist else throw err
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
            userRepository.delete(user);
            return new APIResponse<>(
                    "user deleted successfully",
                    HttpStatus.OK.value(),
                    "SUCCESS",
                    null,
                    null
            );

    }

    @Transactional(transactionManager = "authTransactionManager")
    public APIResponse<FetchUsersDto> toggleActiveStatus(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(!user.isActive());

        User updatedUser = userRepository.save(user);

        FetchUsersDto dto = userMapper.toFetchUsersDto(updatedUser);

        String message = updatedUser.isActive()
                ? "User activated successfully"
                : "User deactivated successfully";

        return new APIResponse<>(
                message,
                HttpStatus.OK.value(),
                "SUCCESS",
                null,
                dto
        );
    }
}
