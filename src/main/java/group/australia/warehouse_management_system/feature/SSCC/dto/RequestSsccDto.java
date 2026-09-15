package group.australia.warehouse_management_system.feature.SSCC.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RequestSsccDto(@NotNull(message = "SSCC is required") @NotEmpty(message = "SSCC is required") String sscc) implements Serializable {
}