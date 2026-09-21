package group.australia.warehouse_management_system.feature.SSCC.mapper;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.entity.SsccEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SsccMapper {
    Sscc toDomain(SsccEntity entity);
    SsccEntity toEntity(Sscc domain);
    Sscc toDomainFromCreate(CreateSsccDto req);
    ResponseSsccDto toDto(Sscc domain);
}
