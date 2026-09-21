package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.entity.ItemEntity;
import group.australia.warehouse_management_system.feature.item.exception.ItemCartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.item.exception.ItemDuplicateException;
import group.australia.warehouse_management_system.feature.item.exception.ItemLocationNotFoundException;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import group.australia.warehouse_management_system.feature.location.entity.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceItemTests {

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CartonHeaderRepositoryPort cartonHeaderRepository;

    private InMemoryItemRepository itemRepository;
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemRepository = new InMemoryItemRepository();
        itemService = new ItemService(itemMapper, itemRepository, locationRepository, cartonHeaderRepository);
    }

    @Test
    void ItemService_CreateItem_ReturnsItem() {
        var req = new ItemRequestDto("ITEM-001", 10, 1L, 1L);

        var location = new Location();
        location.setRow(1);
        location.setSection(2);
        location.setShelf(3);
        location.setLocationCode("1-2-3");

        var cartonHeader = CartonHeader.builder()
                .id(1L).barcode("BC-001").description("Test Carton").version(0).build();

        var itemDomain = Item.builder().itemNumber("ITEM-001").quantity(10).build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(cartonHeaderRepository.findById(1L)).thenReturn(Optional.of(cartonHeader));
        when(itemMapper.toDomainFromCreate(Mockito.any(ItemRequestDto.class))).thenReturn(itemDomain);

        var result = itemService.insertNewItem(req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getItemNumber()).isEqualTo("ITEM-001");
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        Assertions.assertThat(result.getLocation()).isEqualTo(location);
        Assertions.assertThat(result.getCartonHeader()).isEqualTo(cartonHeader);
    }

    @Test
    void ItemService_CreateItem_DuplicateItem_ThrowsConflictException() {
        var location = new Location();
        location.setId(1L);

        itemRepository.save(Item.builder().itemNumber("ITEM-001").location(location).build());

        var req = new ItemRequestDto("ITEM-001", 10, 1L, 1L);

        Assertions.assertThatThrownBy(() -> itemService.insertNewItem(req))
                .isInstanceOf(ItemDuplicateException.class);
    }

    @Test
    void ItemService_CreateItem_LocationNotFound_ThrowsNotFoundException() {
        var req = new ItemRequestDto("ITEM-001", 10, 999L, 1L);

        when(locationRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> itemService.insertNewItem(req))
                .isInstanceOf(ItemLocationNotFoundException.class);
    }

    @Test
    void ItemService_CreateItem_CartonHeaderNotFound_ThrowsNotFoundException() {
        var req = new ItemRequestDto("ITEM-001", 10, 1L, 999L);

        var location = new Location();
        location.setRow(1);
        location.setSection(2);
        location.setShelf(3);
        location.setLocationCode("1-2-3");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(cartonHeaderRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> itemService.insertNewItem(req))
                .isInstanceOf(ItemCartonHeaderNotFoundException.class);
    }
}
