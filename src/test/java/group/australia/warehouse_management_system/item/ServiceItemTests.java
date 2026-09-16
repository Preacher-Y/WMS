package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.entity.Item;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepository;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import group.australia.warehouse_management_system.feature.location.entity.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceItemTests {

    @Mock private ItemRepository itemRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private CartonHeaderRepository cartonHeaderRepository;
    @Mock private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    // ── createItem ──────────────────────────────────────────────────────────

    @Test
    void ItemService_CreateItem_ReturnsResponseItemDto() {
        ItemRequestDto request = new ItemRequestDto("ITEM-001", 10, 1L, 2L);

        Location location = new Location();
        location.setRow(1);
        location.setSection(2);
        location.setShelf(3);
        location.setLocationCode("1-2-3");

        CartonHeader cartonHeader = CartonHeader.builder()
                .id(2L)
                .barcode("BC-001")
                .description("Test Carton")
                .build();

        Item savedItem = Item.builder()
                .id(1L)
                .itemNumber("ITEM-001")
                .quantity(10)
                .location(location)
                .cartonHeader(cartonHeader)
                .version(0)
                .build();

        ResponseItemDto response = ResponseItemDto.builder()
                .id(1L)
                .itemNumber("ITEM-001")
                .quantity(10)
                .locationLocationCode("1-2-3")
                .cartonHeaderBarcode("BC-001")
                .version(0)
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(cartonHeaderRepository.findById(2L)).thenReturn(Optional.of(cartonHeader));
        when(itemRepository.existsByItemNumberAndLocationId("ITEM-001", 1L)).thenReturn(false);
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(savedItem);
        when(itemMapper.toDto(savedItem)).thenReturn(response);

        ResponseItemDto result = itemService.createItem(request);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isGreaterThan(0);
        Assertions.assertThat(result.itemNumber()).isEqualTo("ITEM-001");
        Assertions.assertThat(result.quantity()).isEqualTo(10);
        Assertions.assertThat(result.locationLocationCode()).isEqualTo("1-2-3");
        Assertions.assertThat(result.cartonHeaderBarcode()).isEqualTo("BC-001");
        Assertions.assertThat(result.version()).isZero();
    }

    @Test
    void ItemService_CreateItem_LocationNotFound_ThrowsNotFoundException() {
        ItemRequestDto request = new ItemRequestDto("ITEM-001", 10, 99L, 2L);

        when(locationRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> itemService.createItem(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void ItemService_CreateItem_CartonHeaderNotFound_ThrowsNotFoundException() {
        ItemRequestDto request = new ItemRequestDto("ITEM-001", 10, 1L, 99L);

        Location location = new Location();
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(cartonHeaderRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> itemService.createItem(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void ItemService_CreateItem_DuplicatePair_ThrowsConflictException() {
        ItemRequestDto request = new ItemRequestDto("ITEM-001", 10, 1L, 2L);

        Location location = new Location();
        CartonHeader cartonHeader = CartonHeader.builder().id(2L).barcode("BC-001").description("Test").build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(cartonHeaderRepository.findById(2L)).thenReturn(Optional.of(cartonHeader));
        when(itemRepository.existsByItemNumberAndLocationId("ITEM-001", 1L)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> itemService.createItem(request))
                .isInstanceOf(ConflictException.class);
    }

    // ── updateQuantity ───────────────────────────────────────────────────────

    @Test
    void ItemService_UpdateQuantity_ReturnsResponseItemDto() {
        Location location = new Location();
        location.setLocationCode("1-2-3");

        CartonHeader cartonHeader = CartonHeader.builder()
                .id(2L).barcode("BC-001").description("Test Carton").build();

        Item item = Item.builder()
                .id(1L).itemNumber("ITEM-001").quantity(10)
                .location(location).cartonHeader(cartonHeader).version(0)
                .build();

        ResponseItemDto response = ResponseItemDto.builder()
                .id(1L).itemNumber("ITEM-001").quantity(50)
                .locationLocationCode("1-2-3").cartonHeaderBarcode("BC-001").version(1)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(Mockito.any(Item.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemMapper.toDto(Mockito.any(Item.class))).thenReturn(response);

        ResponseItemDto result = itemService.updateQuantity(1L, 50);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.quantity()).isEqualTo(50);
        Assertions.assertThat(result.version()).isGreaterThan(0);
    }

    @Test
    void ItemService_UpdateQuantity_ItemNotFound_ThrowsNotFoundException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> itemService.updateQuantity(99L, 10))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void ItemService_UpdateQuantity_NegativeValue_ThrowsBadRequestException() {
        Assertions.assertThatThrownBy(() -> itemService.updateQuantity(1L, -1))
                .isInstanceOf(BadRequestException.class);
    }
}
