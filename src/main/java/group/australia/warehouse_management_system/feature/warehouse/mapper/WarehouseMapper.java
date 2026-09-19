package group.australia.warehouse_management_system.feature.warehouse.mapper;

import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    ResponseWarehouseDto toDto(Warehouse warehouse);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Warehouse toEntityFromRequest(CreateWarehouseDto req);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Warehouse toEntityFromUpdate(UpdateWarehouseDto update);
}
