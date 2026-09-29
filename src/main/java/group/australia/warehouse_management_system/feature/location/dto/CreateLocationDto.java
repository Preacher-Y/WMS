package group.australia.warehouse_management_system.feature.location.dto;

import jakarta.validation.constraints.NotNull;

public record CreateLocationDto(
        @NotNull(message = "row is required") Integer row,

        @NotNull(message = "section is required") Integer section,
        @NotNull(message = "shelf is required") Integer shelf) {

}
