package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record UpdateWarehouseDto(@NotNull(message = "The name is required") String name,
                                 boolean active) implements Serializable {
}