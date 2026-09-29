package group.australia.warehouse_management_system.feature.user.dto;

import group.australia.warehouse_management_system.feature.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link group.australia.warehouse_management_system.feature.user.entity.UserEntity}
 */
public record CreateUserDto(
        @NotNull(message = "Email is Required") @Email(message = "Email must be valid") String email,
        @NotNull(message = "Name is Required") @Size(message = "Name must be at least 3 characters", min = 3) @NotBlank(message = "Name can not be blank") String name,
        @NotNull String password,
        Role role) implements Serializable {
}