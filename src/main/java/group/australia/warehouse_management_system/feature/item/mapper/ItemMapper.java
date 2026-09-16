package group.australia.warehouse_management_system.feature.item.mapper;

import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.entity.Item;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(target = "cartonHeaderDescription", source = "cartonHeader.description")
    @Mapping(target = "cartonHeaderBarcode", source = "cartonHeader.barcode")
    @Mapping(target = "locationLocationCode", source = "location.locationCode")
    @Mapping(target = "locationShelf", source = "location.shelf")
    @Mapping(target = "locationSection", source = "location.section")
    @Mapping(target = "locationRow", source = "location.row")
    ResponseItemDto toDto(Item item);
}