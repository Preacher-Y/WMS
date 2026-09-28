package group.australia.warehouse_management_system.feature.user.mapper;

import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.dto.ResponseUserDto;
import group.australia.warehouse_management_system.feature.user.entity.UserEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User user);

    ResponseUserDto toDto(User user);

    User toDomainFromCreate(CreateUserDto req);
}