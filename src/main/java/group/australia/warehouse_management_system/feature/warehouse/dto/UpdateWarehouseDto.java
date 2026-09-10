package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UpdateWarehouseDto(@NotNull(message = "The name is required") String name,
                                 boolean active) implements Serializable {
}