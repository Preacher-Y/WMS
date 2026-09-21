package group.australia.warehouse_management_system.feature.SSCC.mapper;

import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.entity.SSCC;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SsccMapper {
    ResponseSsccDto toDto(SSCC sscc);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SSCC toEntity(CreateSsccDto requestSsccDto);
}