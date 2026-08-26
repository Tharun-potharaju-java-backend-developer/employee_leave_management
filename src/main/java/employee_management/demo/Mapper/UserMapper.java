package employee_management.demo.Mapper;

import employee_management.demo.DTO.FetchUsersDto;
import employee_management.demo.DTO.LoginResponse;
import employee_management.demo.Modal.Permission;
import employee_management.demo.Modal.Role;
import employee_management.demo.Modal.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts the User domain object into API models.
 * Keeping this mapping in one place prevents services from knowing the
 * persistence model of users, roles, and permissions.
 */
@Component
public class UserMapper {

    public FetchUsersDto toFetchUsersDto(User user) {
        FetchUsersDto dto = new FetchUsersDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setAuthProvider(user.getAuthProvider());
        dto.setActive(user.isActive());
        dto.setRoles(roleNames(user));
        dto.setPermissions(permissionNames(user));
        return dto;
    }

    public LoginResponse toLoginResponse(User user, String token, String refreshToken) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRoles(Set.copyOf(roleNames(user)));
        response.setPermissions(Set.copyOf(permissionNames(user)));
        return response;
    }

    private List<String> roleNames(User user) {
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .toList();
    }

    private List<String> permissionNames(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getPermissionName)
                .distinct()
                .toList();
    }
}
