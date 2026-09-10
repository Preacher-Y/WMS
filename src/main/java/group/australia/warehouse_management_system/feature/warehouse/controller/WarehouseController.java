package group.australia.warehouse_management_system.feature.warehouse.controller;

import group.australia.warehouse_management_system.feature.warehouse.dto.RequestWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<ResponseWarehouseDto> createNewWarehouse(
            @RequestBody @Valid RequestWarehouseDto req
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.insertNewWarehouse(req));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseWarehouseDto> updateWarehouse(
            @PathVariable Long id,
            @RequestBody @Valid UpdateWarehouseDto req
    ){
        return ResponseEntity.ok(warehouseService.updateWarehouse(id,req));
    }
}
