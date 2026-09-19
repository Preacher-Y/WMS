package group.australia.warehouse_management_system.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseDuplicateException;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
import group.australia.warehouse_management_system.feature.warehouse.service.WarehouseService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceWarehouseTests {

    private InMemoryWarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        warehouseRepository = new InMemoryWarehouseRepository();
        warehouseService = new WarehouseService(warehouseMapper, warehouseRepository);
    }

    @Test
    void Should_create_warehouse_if_it_does_not_exist_yet() {
        CreateWarehouseDto dto = CreateWarehouseDto.builder()
                .name("Test Warehouse 1")
                .warehouseNumber(1)
                .build();

        Warehouse warehouseDomain = Warehouse.builder()
                .name("Test Warehouse 1")
                .build();

        when(warehouseMapper.toDomainFromCreate(Mockito.any(CreateWarehouseDto.class))).thenReturn(warehouseDomain);

        Warehouse result = warehouseService.insertNewWarehouse(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Test Warehouse 1");
        Assertions.assertThat(result.getWarehouseNumber()).isEqualTo("WH-001");
        Assertions.assertThat(result.isActive()).isTrue();
        Assertions.assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    void Should_update_warehouse_when_it_exists() {
        warehouseRepository.save(Warehouse.builder()
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .version(0)
                .build());

        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Test Warehouse 1 - Updated")
                .active(false)
                .build();

        Warehouse result = warehouseService.updateWarehouse("WH-001", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        Assertions.assertThat(result.getName()).isEqualTo("Test Warehouse 1 - Updated");
        Assertions.assertThat(result.getWarehouseNumber()).isEqualTo("WH-001");
        Assertions.assertThat(result.isActive()).isFalse();
    }

    @Test
    void Should_throw_when_warehouse_number_already_exists() {
        warehouseRepository.save(Warehouse.builder()
                .warehouseNumber("WH-001")
                .name("Existing")
                .build());

        CreateWarehouseDto dto = CreateWarehouseDto.builder()
                .name("Test Warehouse 1")
                .warehouseNumber(1)
                .build();

        Assertions.assertThatThrownBy(() -> warehouseService.insertNewWarehouse(dto))
                .isInstanceOf(WarehouseDuplicateException.class);
    }

    @Test
    void Should_activate_warehouse_on_update() {
        warehouseRepository.save(Warehouse.builder()
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(false)
                .version(0)
                .build());

        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Test Warehouse 1")
                .active(true)
                .build();

        Warehouse result = warehouseService.updateWarehouse("WH-001", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isActive()).isTrue();
    }

    @Test
    void Should_throw_when_updating_non_existent_warehouse() {
        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Any Name")
                .active(true)
                .build();

        Assertions.assertThatThrownBy(() -> warehouseService.updateWarehouse("WH-999", update))
                .isInstanceOf(WarehouseNotFoundException.class);
    }
}
