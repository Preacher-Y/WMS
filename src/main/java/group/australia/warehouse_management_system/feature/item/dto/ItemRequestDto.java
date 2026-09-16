package group.australia.warehouse_management_system.feature.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ItemRequestDto(
        @NotBlank String itemNumber,
        @NotNull @Min(value = 0, message = "Quantity must be a positive number") Integer quantity,
        @NotNull Long locationId,
        @NotNull Long cartonHeaderId
) implements Serializable {
}
