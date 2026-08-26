package employee_management.demo.Controller;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.FetchUsersDto;
import employee_management.demo.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
/*
 * @RestController tells Spring that this class handles HTTP requests.
 *
 * It is a combination of:
 *
 * @Controller
 * +
 * @ResponseBody
 *
 * Every method inside this class automatically returns data
 * (usually JSON) instead of an HTML page.
 *
 * Example:
 *
 * Client (Browser/Postman)
 *          ↓
 * AuthController
 *          ↓
 * AuthService
 *          ↓
 * AuthRepository
 *          ↓
 * Database
 *          ↓
 * JSON Response
 */
public class UserController {

    /*
     * Controller should not contain business logic.
     *
     * Its responsibility is:
     *
     * 1. Receive HTTP requests.
     * 2. Call the appropriate service.
     * 3. Return the response.
     *
     * Business logic belongs in the Service layer.
     *
     * Controller
     *      ↓
     * Service
     *      ↓
     * Repository
     *      ↓
     * Database
     */

    private final UserService userService;

    /*
     * Constructor Dependency Injection.
     *
     * Spring automatically creates an AuthService object
     * because it is annotated with @Service.
     *
     * Since this controller requires AuthService,
     * Spring injects it through the constructor.
     *
     * We never write:
     *
     *      new AuthService(...)
     *
     * Spring manages the object lifecycle.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
     * @GetMapping maps HTTP GET requests to this method.
     *
     * URL:
     * GET http://localhost:8080/users
     *
     * When someone visits this URL,
     * Spring automatically calls this method.
     *
     * Since this is a GET request,
     * it is used only for retrieving data.
     */
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<List<FetchUsersDto>> fetchUsers() {

        /*
         * Delegate the work to the service layer.
         *
         * The controller does not know how data is fetched.
         * It simply asks the service for the required data.
         *
         * AuthService:
         *      ↓
         * Fetches data from Repository
         *      ↓
         * Converts User → FetchUsersDto
         *      ↓
         * Returns List<FetchUsersDto>
         */
        return userService.fetchUsers();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<?>> deleteUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.deleteById(id));
    }
    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<FetchUsersDto>> toggleActiveStatus(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(userService.toggleActiveStatus(id));
    }
}