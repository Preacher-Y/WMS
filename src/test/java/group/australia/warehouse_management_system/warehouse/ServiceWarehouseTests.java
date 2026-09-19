package group.australia.warehouse_management_system.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseDuplicateException;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
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
        CreateWarehouseDto dto = CreateWarehouseDto.builder()
                .name("Test Warehouse 1")
                .warehouseNumber(1)
                .build();

        WarehouseEntity warehouseEntity = WarehouseEntity.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .build();

        Warehouse warehouseDomain = Warehouse.builder()
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

        when(warehouseMapper.toDomainFromCreate(Mockito.any(CreateWarehouseDto.class))).thenReturn(warehouseDomain);
        when(warehouseMapper.toEntity(Mockito.any(Warehouse.class))).thenReturn(warehouseEntity);
        when(warehouseRepository.save(Mockito.any(WarehouseEntity.class))).thenReturn(warehouseEntity);
        when(warehouseMapper.toDomain(Mockito.any(WarehouseEntity.class))).thenReturn(warehouseDomain);
        when(warehouseMapper.toDto(Mockito.any(Warehouse.class))).thenReturn(responseDto);

        ResponseWarehouseDto result = warehouseService.insertNewWarehouse(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.name()).isEqualTo(warehouseEntity.getName());
        Assertions.assertThat(result.warehouseNumber()).isEqualTo(warehouseEntity.getWarehouseNumber());
        Assertions.assertThat(result.active()).isTrue();
        Assertions.assertThat(result.id()).isGreaterThan(0);
    }

    @Test
    void WarehouseService_UpdateWarehouse_ReturnsResponseWarehouseDto() {
        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Test Warehouse 1 - Updated")
                .active(false)
                .build();

        WarehouseEntity warehouseEntity = WarehouseEntity.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .version(0)
                .build();

        Warehouse warehouseDomain = Warehouse.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .version(0)
                .build();

        when(warehouseRepository.findByWarehouseNumber(Mockito.anyString())).thenReturn(Optional.of(warehouseEntity));
        when(warehouseMapper.toDomain(Mockito.any(WarehouseEntity.class))).thenReturn(warehouseDomain);
        when(warehouseMapper.toEntity(Mockito.any(Warehouse.class))).thenReturn(warehouseEntity);
        when(warehouseRepository.save(Mockito.any(WarehouseEntity.class))).thenReturn(warehouseEntity);
        when(warehouseMapper.toDto(Mockito.any(Warehouse.class))).thenReturn(
                ResponseWarehouseDto.builder()
                        .id(1L)
                        .name("Test Warehouse 1 - Updated")
                        .warehouseNumber("WH-001")
                        .version(1)
                        .active(false)
                        .build()
        );

        ResponseWarehouseDto result = warehouseService.updateWarehouse("WH-001", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(1L);
        Assertions.assertThat(result.name()).isEqualTo("Test Warehouse 1 - Updated");
        Assertions.assertThat(result.warehouseNumber()).isEqualTo("WH-001");
        Assertions.assertThat(result.version()).isGreaterThan(0);
        Assertions.assertThat(result.active()).isFalse();
    }

    @Test
    void WarehouseService_CreateWarehouse_DuplicateWarehouseNumber_ThrowsConflictException() {
        CreateWarehouseDto dto = CreateWarehouseDto.builder()
                .name("Test Warehouse 1")
                .warehouseNumber(1)
                .build();

        when(warehouseRepository.existsByWarehouseNumber("WH-001")).thenReturn(true);

        Assertions.assertThatThrownBy(() -> warehouseService.insertNewWarehouse(dto))
                .isInstanceOf(WarehouseDuplicateException.class);
    }

    @Test
    void WarehouseService_UpdateWarehouse_ActivateWarehouse_ReturnsActiveDto() {
        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Test Warehouse 1")
                .active(true)
                .build();

        WarehouseEntity warehouseEntity = WarehouseEntity.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(false)
                .version(0)
                .build();

        Warehouse warehouseDomain = Warehouse.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(false)
                .version(0)
                .build();

        when(warehouseRepository.findByWarehouseNumber(Mockito.anyString())).thenReturn(Optional.of(warehouseEntity));
        when(warehouseMapper.toDomain(Mockito.any(WarehouseEntity.class))).thenReturn(warehouseDomain);
        when(warehouseMapper.toEntity(Mockito.any(Warehouse.class))).thenReturn(warehouseEntity);
        when(warehouseRepository.save(Mockito.any(WarehouseEntity.class))).thenReturn(warehouseEntity);
        when(warehouseMapper.toDto(Mockito.any(Warehouse.class))).thenReturn(
                ResponseWarehouseDto.builder()
                        .id(1L)
                        .name("Test Warehouse 1")
                        .warehouseNumber("WH-001")
                        .active(true)
                        .version(0)
                        .build()
        );

        ResponseWarehouseDto result = warehouseService.updateWarehouse("WH-001", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.active()).isTrue();
    }

    @Test
    void WarehouseService_UpdateWarehouse_WarehouseNotFound_ThrowsNotFoundException() {
        UpdateWarehouseDto update = UpdateWarehouseDto.builder()
                .name("Any Name")
                .active(true)
                .build();

        when(warehouseRepository.findByWarehouseNumber(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> warehouseService.updateWarehouse("WH-999", update))
                .isInstanceOf(WarehouseNotFoundException.class);
    }
}
