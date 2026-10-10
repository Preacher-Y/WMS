package group.australia.warehouse_management_system.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull(message = "Email is required") @Email(message = "Email must be valid") String email,
        @NotNull(message = "Password is required") String password) {
}
