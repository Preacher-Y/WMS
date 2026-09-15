package group.australia.warehouse_management_system.feature.SSCC.dto;

import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record ResponseSsccDto(Long id, String sscc, ResponseCartonHeaderDto cartonHeader,
                              LocalDateTime receivedTimestamp) implements Serializable {
}