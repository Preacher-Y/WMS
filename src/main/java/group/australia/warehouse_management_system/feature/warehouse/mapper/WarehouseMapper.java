package group.australia.warehouse_management_system.feature.warehouse.mapper;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WarehouseMapper {

    Warehouse toDomain(WarehouseEntity warehouseEntity);

    WarehouseEntity toEntity(Warehouse warehouse);

    @Mapping(target = "warehouseNumber", ignore = true)
    Warehouse toDomainFromCreate(CreateWarehouseDto req);

    ResponseWarehouseDto toDto(Warehouse warehouse);
}
