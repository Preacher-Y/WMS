package group.australia.warehouse_management_system.feature.moveItem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestMoveItemDto(
        @NotNull(message = "Warehouse Number is required") @NotEmpty(message = "Warehouse Number is required") String warehouseNumber,
        @NotNull(message = "Item Number is required") @NotEmpty(message = "Item Number is required") String itemNumber,
        @NotNull(message = "From Location is required") @NotEmpty(message = "From Location is required") String fromLocationCode,
        @NotNull(message = "To Location is required") @NotEmpty(message = "To Location is required") String toLocationCode,
        @Min(value = 1,message = "Quantity must be at least 1") Integer quantity
) {
}
