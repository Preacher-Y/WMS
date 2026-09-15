package group.australia.warehouse_management_system.feature.location.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import group.australia.warehouse_management_system.feature.location.dto.LocationRequestDto;
import group.australia.warehouse_management_system.feature.location.dto.LocationResponseDto;
import group.australia.warehouse_management_system.feature.location.entity.Location;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    Location toEntityFromRequest(LocationRequestDto request);

    @Mapping(source = "warehouse.id", target = "warehouseId")
    LocationResponseDto toResponseDto(Location location);
}






    

