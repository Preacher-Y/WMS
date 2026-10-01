package group.australia.warehouse_management_system.feature.recieve.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRecieverDto(
        @NotBlank(message = "warehouseNumber is required")
        String warehouseNumber,

        @NotBlank(message = "sscc is required")
        String sscc,

        @NotBlank(message = "locationCode is required")
        String locationCode
) {
}