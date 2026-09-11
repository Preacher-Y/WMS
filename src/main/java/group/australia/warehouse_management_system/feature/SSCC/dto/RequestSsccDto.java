package group.australia.warehouse_management_system.feature.SSCC.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link group.australia.warehouse_management_system.feature.SSCC.entity.SSCC}
 */
public record RequestSsccDto(@NotNull(message = "SSCC is required") @NotEmpty(message = "SSCC is required") String sscc) implements Serializable {
}