package group.australia.warehouse_management_system.feature.auth.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshRequestDto(
        @NotNull(message = "Refresh token is required") String refreshToken) {
}
