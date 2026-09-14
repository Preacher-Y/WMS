package group.australia.warehouse_management_system.feature.cartonHeader.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record UpdateCartonHeaderDto(
        @NotNull(message = "Description is required") @NotBlank String description) implements Serializable {
}