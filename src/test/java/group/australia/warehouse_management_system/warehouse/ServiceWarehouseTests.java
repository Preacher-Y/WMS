package group.australia.warehouse_management_system.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.dto.RequestWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import group.australia.warehouse_management_system.feature.warehouse.service.WarehouseService;
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
class ServiceWarehouseTests {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void WarehouseService_CreateWarehouse_ReturnsResponseWarehouseDto() {
        RequestWarehouseDto dto = RequestWarehouseDto.builder()
                .name("Test Warehouse 1")
                .warehouseNumber("1")
                .build();

        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .build();

        ResponseWarehouseDto responseDto = ResponseWarehouseDto.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .build();

        when(warehouseMapper.toEntityFromRequest(Mockito.any(RequestWarehouseDto.class))).thenReturn(warehouse);
        when(warehouseRepository.save(Mockito.any(Warehouse.class))).thenReturn(warehouse);
        when(warehouseMapper.toDto(Mockito.any(Warehouse.class))).thenReturn(responseDto);

        ResponseWarehouseDto result = warehouseService.insertNewWarehouse(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.name()).isEqualTo(warehouse.getName());
        Assertions.assertThat(result.warehouseNumber()).isEqualTo(warehouse.getWarehouseNumber());
        Assertions.assertThat(result.active()).isTrue();
        Assertions.assertThat(result.id()).isGreaterThan(0);
    }

    @Test
    void WarehouseService_UpdateWarehouse_ReturnsResponseWarehouseDto() {
        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Test Warehouse 1 - Updated")
                .active(false)
                .build();

        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .build();

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(Mockito.any(Warehouse.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(warehouseMapper.toDto(Mockito.any(Warehouse.class)))
                .thenAnswer(invocation -> {
                    Warehouse savedWarehouse = invocation.getArgument(0);

                    return ResponseWarehouseDto.builder()
                            .id(savedWarehouse.getId())
                            .name(savedWarehouse.getName())
                            .warehouseNumber(savedWarehouse.getWarehouseNumber())
                            .active(savedWarehouse.isActive())
                            .build();
                });

        ResponseWarehouseDto result = warehouseService.updateWarehouse(1L, update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(1L);
        Assertions.assertThat(result.name()).isEqualTo("Test Warehouse 1 - Updated");
        Assertions.assertThat(result.warehouseNumber()).isEqualTo("WH-001");
        Assertions.assertThat(result.active()).isFalse();

    }
}
