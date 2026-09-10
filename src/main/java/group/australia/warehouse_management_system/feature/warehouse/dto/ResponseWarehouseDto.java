package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ResponseWarehouseDto(Long id, @NotNull String warehouseNumber, @NotNull String name, boolean active,
                                   Integer version) implements Serializable {
}