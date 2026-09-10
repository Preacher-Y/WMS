package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record WarehouseRequestDto(@NotNull String warehouseNumber, @NotNull String name) implements Serializable {
}