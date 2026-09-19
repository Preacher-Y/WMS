package group.australia.warehouse_management_system.feature.warehouse.service;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
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

    public ResponseWarehouseDto insertNewWarehouse(CreateWarehouseDto req) {

        String warehouseNumber = String.format(
                "WH-%03d",
                req.warehouseNumber()
        );

        if (warehouseRepository.existsByWarehouseNumber(warehouseNumber)) {
            throw new ConflictException(
                    "Warehouse number already exists: " + warehouseNumber
            );
        }

        Warehouse warehouse = warehouseMapper.toEntityFromRequest(req);
        warehouse.setWarehouseNumber(warehouseNumber);

        try {
            Warehouse savedWarehouse = warehouseRepository.save(warehouse);
            return warehouseMapper.toDto(savedWarehouse);

        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "Warehouse number already exists: " + warehouseNumber
            );
        }
    }

    public ResponseWarehouseDto updateWarehouse(
            String warehouseNumber,
            UpdateWarehouseDto update
    ) {

        Warehouse warehouse = warehouseRepository.findByWarehouseNumber(warehouseNumber)
                .orElseThrow(() ->
                        new NotFoundException("Warehouse not found")
                );

        warehouse.setName(update.name());
        warehouse.setActive(update.active());

        try {
            Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
            return warehouseMapper.toDto(updatedWarehouse);

        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Warehouse update conflicts with existing data");
        }
    }
}