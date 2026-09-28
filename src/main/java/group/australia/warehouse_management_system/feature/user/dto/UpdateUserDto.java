package group.australia.warehouse_management_system.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UpdateUserDto(

        @NotNull(message = "Name is required")
        @Size(message = "Name must be  at least 3 characters", min = 3)
        @NotBlank(message = "Name can not be blank")
        String name,

        @NotNull
        Boolean active) implements Serializable {
}