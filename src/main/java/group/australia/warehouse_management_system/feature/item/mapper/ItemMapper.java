package group.australia.warehouse_management_system.feature.item.mapper;

import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.entity.ItemEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {
    Item toDomain(ItemEntity entity);
    ItemEntity toEntity(Item domain);
    Item toDomainFromCreate(ItemRequestDto req);

    @Mapping(target = "cartonHeaderDescription", source = "cartonHeader.description")
    @Mapping(target = "cartonHeaderBarcode", source = "cartonHeader.barcode")
    @Mapping(target = "locationLocationCode", source = "location.locationCode")
    @Mapping(target = "locationShelf", source = "location.shelf")
    @Mapping(target = "locationSection", source = "location.section")
    @Mapping(target = "locationRow", source = "location.row")
    ResponseItemDto toDto(Item domain);
}
