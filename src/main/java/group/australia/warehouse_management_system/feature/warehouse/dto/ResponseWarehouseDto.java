package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResponseWarehouseDto(Long id, @NotNull String warehouseNumber, @NotNull String name, boolean active,
                                   Integer version) implements Serializable {
}