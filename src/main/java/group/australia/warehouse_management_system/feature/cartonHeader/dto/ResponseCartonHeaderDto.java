package group.australia.warehouse_management_system.feature.cartonHeader.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResponseCartonHeaderDto(Long id, String barcode, String description,
                                      Integer version) implements Serializable {
}