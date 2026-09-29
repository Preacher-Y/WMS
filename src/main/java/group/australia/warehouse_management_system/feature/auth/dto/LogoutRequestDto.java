package group.australia.warehouse_management_system.feature.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LogoutRequestDto(
        @NotNull(message = "Refresh token is required") String refreshToken) {
}
