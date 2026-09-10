package group.australia.warehouse_management_system.feature.warehouse.service;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.dto.RequestWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepository warehouseRepository;

    public ResponseWarehouseDto insertNewWarehouse(RequestWarehouseDto req){
        Warehouse warehouse = warehouseMapper.toEntityFromRequest(req);
        warehouse.setWarehouseNumber(String.format(
                "WH-%03d", Integer.parseInt(warehouse.getWarehouseNumber())
        ));

        return warehouseMapper.toDto(warehouseRepository.save(warehouse));
    }

    public ResponseWarehouseDto updateWarehouse(Long id, UpdateWarehouseDto update){

        Optional<Warehouse> warehouse = warehouseRepository.findById(id);
        if (warehouse.isEmpty()) {
            throw new NotFoundException("Warehouse not found");
        }

        warehouse.get().setName(update.name());
        warehouse.get().setActive(update.active());

        return  warehouseMapper.toDto(warehouseRepository.save(warehouse.get()));
    }
}
