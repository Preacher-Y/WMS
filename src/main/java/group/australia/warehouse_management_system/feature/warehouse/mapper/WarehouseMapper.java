package group.australia.warehouse_management_system.feature.warehouse.mapper;

import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    ResponseWarehouseDto toDto(WarehouseEntity warehouse);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    WarehouseEntity toEntityFromRequest(CreateWarehouseDto req);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    WarehouseEntity toEntityFromUpdate(UpdateWarehouseDto update);
}
