package group.australia.warehouse_management_system.feature.warehouse.service;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseDuplicateException;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepositoryPort warehouseRepository;

    public Warehouse insertNewWarehouse(CreateWarehouseDto req) {

        var warehouseNumber = String.format(
                "WH-%03d",
                req.warehouseNumber()
        );

        if (warehouseRepository.existsByWarehouseNumber(warehouseNumber)) {
            throw new WarehouseDuplicateException(warehouseNumber);
        }

        var warehouse = warehouseMapper.toDomainFromCreate(req)
                .toBuilder()
                .warehouseNumber(warehouseNumber)
                .build();

        try {
            return warehouseRepository.save(warehouse);

        } catch (DataIntegrityViolationException ex) {
            throw new WarehouseDuplicateException(warehouseNumber);
        }
    }

    public Warehouse updateWarehouse(
            String warehouseNumber,
            UpdateWarehouseDto update
    ) {

        var warehouse = warehouseRepository.findByWarehouseNumber(warehouseNumber)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseNumber));

        var renamed = warehouse.rename(update.name());
        var updated = update.active() ? renamed.activate() : renamed.deactivate();

        try {
            return warehouseRepository.save(updated);

        } catch (DataIntegrityViolationException ex) {
            throw new WarehouseDuplicateException(warehouseNumber);
        }
    }
}
