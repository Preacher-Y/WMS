package group.australia.warehouse_management_system.feature.warehouse.service;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseDuplicateException;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepository warehouseRepository;

    public Warehouse insertNewWarehouse(CreateWarehouseDto req) {

        String warehouseNumber = String.format(
                "WH-%03d",
                req.warehouseNumber()
        );

        if (warehouseRepository.existsByWarehouseNumber(warehouseNumber)) {
            throw new WarehouseDuplicateException(warehouseNumber);
        }

        Warehouse warehouse = warehouseMapper.toDomainFromCreate(req)
                .toBuilder()
                .warehouseNumber(warehouseNumber)
                .build();

        try {
            WarehouseEntity saved = warehouseRepository.save(warehouseMapper.toEntity(warehouse));
            return warehouseMapper.toDomain(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new WarehouseDuplicateException(warehouseNumber);
        }
    }

    public Warehouse updateWarehouse(
            String warehouseNumber,
            UpdateWarehouseDto update
    ) {

        Warehouse warehouse = warehouseMapper.toDomain(
                warehouseRepository.findByWarehouseNumber(warehouseNumber)
                        .orElseThrow(() -> new WarehouseNotFoundException(warehouseNumber))
        );

        Warehouse renamed = warehouse.rename(update.name());
        Warehouse updated = update.active() ? renamed.activate() : renamed.deactivate();

        try {
            WarehouseEntity saved = warehouseRepository.save(warehouseMapper.toEntity(updated));
            return warehouseMapper.toDomain(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new WarehouseDuplicateException(warehouseNumber);
        }
    }
}
