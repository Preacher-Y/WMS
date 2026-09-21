package group.australia.warehouse_management_system.feature.cartonHeader.mapper;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartonHeaderMapper {

    CartonHeader toDomain(CartonHeaderEntity entity);

    CartonHeaderEntity toEntity(CartonHeader domain);

    CartonHeader toDomainFromCreate(CreateCartonHeaderDto req);

    ResponseCartonHeaderDto toDto(CartonHeader domain);
}
