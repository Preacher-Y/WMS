package group.australia.warehouse_management_system.moveItem;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepositoryAdapter;
import group.australia.warehouse_management_system.feature.moveItem.dto.RequestMoveItemDto;
import group.australia.warehouse_management_system.feature.moveItem.exception.MoveItemInsufficientStockException;
import group.australia.warehouse_management_system.feature.moveItem.service.MoveItemService;
import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceMoveItemTests {

    private InMemoryItemRepository itemRepository;

    @Mock
    private WarehouseRepositoryPort warehouseRepository;

    @Mock
    private LocationRepositoryAdapter locationRepositoryAdapter;

    private MoveItemService moveItemService;

    private static final String WAREHOUSE_NUMBER = "WH-001";
    private static final Long WAREHOUSE_ID = 1L;
    private static final String FROM_CODE = "1-1-1";
    private static final String TO_CODE = "2-2-2";
    private static final String ITEM_NUMBER = "ITEM-001";

    @BeforeEach
    void setUp() {
        itemRepository = new InMemoryItemRepository();
        moveItemService = new MoveItemService(warehouseRepository, locationRepositoryAdapter, new ItemService(itemRepository));
    }

    @Test
    void Should_reduce_source_and_increment_destination_when_destination_item_exists() {
        var warehouse = activeWarehouse();
        var srcLoc = locationWithId(10L);
        var dstLoc = locationWithId(20L);

        var cartonHeader = CartonHeader.builder().id(1L).barcode("BC-001").description("Test").build();
        itemRepository.save(Item.builder().id(1L).itemNumber(ITEM_NUMBER).quantity(10)
                .location(srcLoc).cartonHeader(cartonHeader).version(0).build());
        itemRepository.save(Item.builder().id(2L).itemNumber(ITEM_NUMBER).quantity(5)
                .location(dstLoc).cartonHeader(cartonHeader).version(0).build());

        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(warehouse));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, FROM_CODE)).thenReturn(Optional.of(srcLoc));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, TO_CODE)).thenReturn(Optional.of(dstLoc));

        moveItemService.moveItem(request(3));

        var updatedSrc = itemRepository.findByItemNumberAndLocationId(ITEM_NUMBER, 10L).orElseThrow();
        var updatedDst = itemRepository.findByItemNumberAndLocationId(ITEM_NUMBER, 20L).orElseThrow();

        Assertions.assertThat(updatedSrc.getQuantity()).isEqualTo(7);
        Assertions.assertThat(updatedDst.getQuantity()).isEqualTo(8);
    }

    @Test
    void Should_reduce_source_and_create_destination_when_destination_item_not_exist() {
        var warehouse = activeWarehouse();
        var srcLoc = locationWithId(10L);
        var dstLoc = locationWithId(20L);

        var cartonHeader = CartonHeader.builder().id(1L).barcode("BC-001").description("Test").build();
        itemRepository.save(Item.builder().id(1L).itemNumber(ITEM_NUMBER).quantity(10)
                .location(srcLoc).cartonHeader(cartonHeader).version(0).build());

        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(warehouse));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, FROM_CODE)).thenReturn(Optional.of(srcLoc));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, TO_CODE)).thenReturn(Optional.of(dstLoc));

        moveItemService.moveItem(request(4));

        var updatedSrc = itemRepository.findByItemNumberAndLocationId(ITEM_NUMBER, 10L).orElseThrow();
        var newDst = itemRepository.findByItemNumberAndLocationId(ITEM_NUMBER, 20L).orElseThrow();

        Assertions.assertThat(updatedSrc.getQuantity()).isEqualTo(6);
        Assertions.assertThat(newDst.getQuantity()).isEqualTo(4);
        Assertions.assertThat(newDst.getCartonHeader().getId()).isEqualTo(1L);
    }

    @Test
    void Should_throw_when_source_quantity_is_insufficient() {
        var warehouse = activeWarehouse();
        var srcLoc = locationWithId(10L);
        var dstLoc = locationWithId(20L);

        var cartonHeader = CartonHeader.builder().id(1L).barcode("BC-001").description("Test").build();
        itemRepository.save(Item.builder().id(1L).itemNumber(ITEM_NUMBER).quantity(5)
                .location(srcLoc).cartonHeader(cartonHeader).version(0).build());

        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(warehouse));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, FROM_CODE)).thenReturn(Optional.of(srcLoc));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, TO_CODE)).thenReturn(Optional.of(dstLoc));

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(request(10)))
                .isInstanceOf(MoveItemInsufficientStockException.class);
    }

    @Test
    void Should_throw_when_warehouse_is_not_active() {
        var inactiveWarehouse = Warehouse.builder()
                .id(WAREHOUSE_ID).warehouseNumber(WAREHOUSE_NUMBER).name("Test").active(false).build();

        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(inactiveWarehouse));

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(request(1)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void Should_throw_when_warehouse_not_found() {
        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(request(1)))
                .isInstanceOf(WarehouseNotFoundException.class);
    }

    @Test
    void Should_throw_when_source_location_not_found() {
        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(activeWarehouse()));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, FROM_CODE)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(request(1)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void Should_throw_when_destination_location_not_found() {
        var srcLoc = locationWithId(10L);

        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(activeWarehouse()));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, FROM_CODE)).thenReturn(Optional.of(srcLoc));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, TO_CODE)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(request(1)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void Should_throw_when_source_item_not_found() {
        var srcLoc = locationWithId(10L);
        var dstLoc = locationWithId(20L);

        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(activeWarehouse()));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, FROM_CODE)).thenReturn(Optional.of(srcLoc));
        when(locationRepositoryAdapter.findByLocationCode(WAREHOUSE_ID, TO_CODE)).thenReturn(Optional.of(dstLoc));

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(request(1)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void Should_throw_when_source_and_destination_locations_are_the_same() {
        when(warehouseRepository.findByWarehouseNumber(WAREHOUSE_NUMBER)).thenReturn(Optional.of(activeWarehouse()));

        var sameLocationRequest = RequestMoveItemDto.builder()
                .warehouseNumber(WAREHOUSE_NUMBER)
                .itemNumber(ITEM_NUMBER)
                .fromLocationCode(FROM_CODE)
                .toLocationCode(FROM_CODE)
                .quantity(1)
                .build();

        Assertions.assertThatThrownBy(() -> moveItemService.moveItem(sameLocationRequest))
                .isInstanceOf(BadRequestException.class);
    }

    private Warehouse activeWarehouse() {
        return Warehouse.builder()
                .id(WAREHOUSE_ID)
                .warehouseNumber(WAREHOUSE_NUMBER)
                .name("Test Warehouse")
                .active(true)
                .build();
    }

    private Location locationWithId(Long id) {
        var loc = new Location();
        loc.setId(id);
        loc.setRow(1);
        loc.setSection(1);
        loc.setShelf(1);
        loc.setWarehouseId(WAREHOUSE_ID);
        return loc;
    }

    private RequestMoveItemDto request(int quantity) {
        return RequestMoveItemDto.builder()
                .warehouseNumber(WAREHOUSE_NUMBER)
                .itemNumber(ITEM_NUMBER)
                .fromLocationCode(FROM_CODE)
                .toLocationCode(TO_CODE)
                .quantity(quantity)
                .build();
    }
}
