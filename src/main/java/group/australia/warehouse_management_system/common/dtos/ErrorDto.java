package group.australia.warehouse_management_system.common.dtos;

import jakarta.validation.constraints.NotNull;

public record ErrorDto(@NotNull String error) {
}

