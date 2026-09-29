package group.australia.warehouse_management_system.feature.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.dto.CreateLocationDto;
import group.australia.warehouse_management_system.feature.location.dto.LocationResponseDto;
import group.australia.warehouse_management_system.feature.location.entity.LocationEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "warehouseId", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    Location toDomain(CreateLocationDto request);

    @Mapping(target = "warehouse", ignore = true)
    LocationEntity toEntity(Location location);
    @Mapping(source = "warehouse.id", target = "warehouseId")
    Location toDomain(LocationEntity entity);

    LocationResponseDto toResponseDto(Location location);
}