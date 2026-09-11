package group.australia.warehouse_management_system.feature.cartonHeader.dto;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;

import java.io.Serializable;

/**
 * DTO for {@link CartonHeader}
 */
public record ResponseCartonHeaderDto(Long id, String barcode, String description,
                                      Integer version) implements Serializable {
}