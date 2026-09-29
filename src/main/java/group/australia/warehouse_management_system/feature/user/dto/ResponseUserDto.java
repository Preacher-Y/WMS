package group.australia.warehouse_management_system.feature.user.dto;

import group.australia.warehouse_management_system.feature.user.domain.Role;

import java.io.Serializable;

public record ResponseUserDto(Long id, String email, String name, Boolean active, Role role) implements Serializable {
}