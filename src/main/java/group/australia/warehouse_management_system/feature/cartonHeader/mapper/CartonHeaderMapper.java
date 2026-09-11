package group.australia.warehouse_management_system.feature.cartonHeader.mapper;

import group.australia.warehouse_management_system.feature.cartonHeader.dto.RequestCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartonHeaderMapper {

    ResponseCartonHeaderDto toDto(CartonHeader cartonHeader);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CartonHeader toEntity(RequestCartonHeaderDto req);
}