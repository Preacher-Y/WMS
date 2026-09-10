package group.australia.warehouse_management_system.feature.warehouse.mapper;

import group.australia.warehouse_management_system.feature.warehouse.dto.WarehouseRequestDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.WarehouseResponseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    WarehouseResponseDto toDto(Warehouse warehouse);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Warehouse toEntity(WarehouseRequestDto req);
}
