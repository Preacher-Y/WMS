package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RequestWarehouseDto(@NotNull String warehouseNumber, @NotNull String name) implements Serializable {
}