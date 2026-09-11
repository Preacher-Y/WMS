package group.australia.warehouse_management_system.feature.cartonHeader.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader}
 */
public record RequestCartonHeaderDto(@NotNull(message = "Barcode is required") String barcode,
                                     @NotNull String description) implements Serializable {
}