package group.australia.warehouse_management_system.feature.user.dto;

import java.io.Serializable;

public record ResponseUserDto(Long id, String email, String name, Boolean active) implements Serializable {
}