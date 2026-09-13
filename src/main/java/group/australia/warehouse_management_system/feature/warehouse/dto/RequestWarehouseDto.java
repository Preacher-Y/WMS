package group.australia.warehouse_management_system.feature.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;

@Builder
public record RequestWarehouseDto(@NotNull(message = "Warehouse Number is required") Integer warehouseNumber,
                                  @NotNull(message = "Warehouse Name is required") String name)
        implements Serializable {
}