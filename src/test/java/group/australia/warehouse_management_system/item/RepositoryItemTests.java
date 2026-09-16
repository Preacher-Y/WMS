package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
import group.australia.warehouse_management_system.feature.item.entity.Item;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepository;
import group.australia.warehouse_management_system.feature.location.entity.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RepositoryItemTests {

    @Autowired private ItemRepository itemRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private CartonHeaderRepository cartonHeaderRepository;
    @Autowired private WarehouseRepository warehouseRepository;

    private Item savedItem;
    private Location savedLocation;
    private CartonHeader savedCartonHeader;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = warehouseRepository.save(Warehouse.builder()
                .warehouseNumber("WH-TEST-001")
                .name("Test Warehouse")
                .active(true)
                .build());

        savedLocation = new Location();
        savedLocation.setRow(1);
        savedLocation.setSection(2);
        savedLocation.setShelf(3);
        savedLocation.setLocationCode("1-2-3");
        savedLocation.setWarehouse(warehouse);
        savedLocation = locationRepository.save(savedLocation);

        savedCartonHeader = cartonHeaderRepository.save(CartonHeader.builder()
                .barcode("BC-TEST-001")
                .description("Test Carton")
                .build());

        savedItem = itemRepository.save(Item.builder()
                .itemNumber("ITEM-001")
                .quantity(10)
                .location(savedLocation)
                .cartonHeader(savedCartonHeader)
                .build());
    }

    @Test
    void ItemRepository_Save_ReturnsItem() {
        Assertions.assertThat(savedItem).isNotNull();
        Assertions.assertThat(savedItem.getId()).isGreaterThan(0);
        Assertions.assertThat(savedItem.getItemNumber()).isEqualTo("ITEM-001");
        Assertions.assertThat(savedItem.getQuantity()).isEqualTo(10);
    }

    @Test
    void ItemRepository_FindById_VerifiesLocationAndCartonHeaderRelationships() {
        Optional<Item> result = itemRepository.findById(savedItem.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getLocation().getId()).isEqualTo(savedLocation.getId());
        Assertions.assertThat(result.get().getLocation().getLocationCode()).isEqualTo("1-2-3");
        Assertions.assertThat(result.get().getCartonHeader().getId()).isEqualTo(savedCartonHeader.getId());
        Assertions.assertThat(result.get().getCartonHeader().getBarcode()).isEqualTo("BC-TEST-001");
    }

    @Test
    void ItemRepository_ExistsByItemNumberAndLocationId_ReturnsTrue() {
        boolean exists = itemRepository.existsByItemNumberAndLocationId("ITEM-001", savedLocation.getId());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void ItemRepository_ExistsByItemNumberAndLocationId_UnknownPair_ReturnsFalse() {
        boolean exists = itemRepository.existsByItemNumberAndLocationId("ITEM-999", savedLocation.getId());

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void ItemRepository_FindByItemNumberAndLocation_ReturnsItem() {
        Optional<Item> result = itemRepository.findByItemNumberAndLocation("ITEM-001", savedLocation);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getItemNumber()).isEqualTo("ITEM-001");
        Assertions.assertThat(result.get().getQuantity()).isEqualTo(10);
    }

    @Test
    void ItemRepository_DuplicateItemNumberAndLocation_ThrowsDataIntegrityViolationException() {
        Item duplicate = Item.builder()
                .itemNumber("ITEM-001")
                .quantity(5)
                .location(savedLocation)
                .cartonHeader(savedCartonHeader)
                .build();

        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void ItemRepository_StaleVersion_ThrowsOptimisticLockingFailureException() {
        Integer originalVersion = savedItem.getVersion();

        savedItem.setQuantity(20);
        itemRepository.saveAndFlush(savedItem);

        Item stale = Item.builder()
                .id(savedItem.getId())
                .itemNumber(savedItem.getItemNumber())
                .quantity(99)
                .location(savedLocation)
                .cartonHeader(savedCartonHeader)
                .version(originalVersion)
                .build();

        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
