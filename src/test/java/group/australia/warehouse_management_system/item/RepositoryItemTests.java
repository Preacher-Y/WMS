package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderJpaRepository;
import group.australia.warehouse_management_system.feature.item.entity.ItemEntity;
import group.australia.warehouse_management_system.feature.item.repository.ItemJpaRepository;
import group.australia.warehouse_management_system.feature.location.entity.LocationEntity;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseJpaRepository;
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

    @Autowired private ItemJpaRepository itemRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private CartonHeaderJpaRepository cartonHeaderRepository;
    @Autowired private WarehouseJpaRepository warehouseRepository;

    private ItemEntity savedItem;
    private LocationEntity savedLocation;
    private CartonHeaderEntity savedCartonHeader;

    @BeforeEach
    void setUp() {
        var warehouse = warehouseRepository.save(WarehouseEntity.builder()
                .warehouseNumber("WH-TEST-001")
                .name("Test Warehouse")
                .active(true)
                .build());

        savedLocation = new LocationEntity();
        savedLocation.setRow(1);
        savedLocation.setSection(2);
        savedLocation.setShelf(3);
        savedLocation.setWarehouse(warehouse);
        savedLocation = locationRepository.save(savedLocation);

        savedCartonHeader = cartonHeaderRepository.save(CartonHeaderEntity.builder()
                .barcode("BC-TEST-001")
                .description("Test Carton")
                .build());

        savedItem = itemRepository.save(ItemEntity.builder()
                .itemNumber("ITEM-001")
                .quantity(10)
                .location(savedLocation)
                .cartonHeader(savedCartonHeader)
                .build());
    }

    @Test
    void Should_save_item_and_return_persisted_entity() {
        Assertions.assertThat(savedItem).isNotNull();
        Assertions.assertThat(savedItem.getId()).isGreaterThan(0);
        Assertions.assertThat(savedItem.getItemNumber()).isEqualTo("ITEM-001");
        Assertions.assertThat(savedItem.getQuantity()).isEqualTo(10);
    }

    @Test
    void Should_find_item_with_location_and_carton_header_relationships() {
        Optional<ItemEntity> result = itemRepository.findById(savedItem.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getLocation().getId()).isEqualTo(savedLocation.getId());
        Assertions.assertThat(result.get().getLocation().getRow()).isEqualTo(1);
        Assertions.assertThat(result.get().getLocation().getSection()).isEqualTo(2);
        Assertions.assertThat(result.get().getLocation().getShelf()).isEqualTo(3);
        Assertions.assertThat(result.get().getCartonHeader().getId()).isEqualTo(savedCartonHeader.getId());
        Assertions.assertThat(result.get().getCartonHeader().getBarcode()).isEqualTo("BC-TEST-001");
    }

    @Test
    void Should_return_true_when_item_exists_at_location() {
        boolean exists = itemRepository.existsByItemNumberAndLocationId("ITEM-001", savedLocation.getId());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void Should_return_false_when_item_number_location_pair_unknown() {
        boolean exists = itemRepository.existsByItemNumberAndLocationId("ITEM-999", savedLocation.getId());

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void Should_return_true_when_item_number_exists() {
        boolean exists = itemRepository.existsByItemNumber("ITEM-001");

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void Should_return_false_when_item_number_unknown() {
        boolean exists = itemRepository.existsByItemNumber("ITEM-999");

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void Should_find_item_by_item_number_and_location() {
        Optional<ItemEntity> result = itemRepository.findByItemNumberAndLocation("ITEM-001", savedLocation);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getItemNumber()).isEqualTo("ITEM-001");
        Assertions.assertThat(result.get().getQuantity()).isEqualTo(10);
    }

    @Test
    void Should_throw_when_duplicate_item_number_and_location() {
        var duplicate = ItemEntity.builder()
                .itemNumber("ITEM-001")
                .quantity(5)
                .location(savedLocation)
                .cartonHeader(savedCartonHeader)
                .build();

        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void Should_throw_when_saving_stale_version() {
        var originalVersion = savedItem.getVersion();

        savedItem.setQuantity(20);
        itemRepository.saveAndFlush(savedItem);

        var stale = ItemEntity.builder()
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
