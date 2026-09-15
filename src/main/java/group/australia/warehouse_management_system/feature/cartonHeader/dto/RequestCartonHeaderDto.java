package group.australia.warehouse_management_system.feature.cartonHeader.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RequestCartonHeaderDto(@NotNull(message = "Barcode is required") String barcode,
                                     @NotNull String description) implements Serializable {
}