package group.australia.warehouse_management_system.feature.warehouse.controller;

import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
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
    private final WarehouseMapper warehouseMapper;

    @PostMapping
    public ResponseEntity<ResponseWarehouseDto> createNewWarehouse(
            @RequestBody @Valid CreateWarehouseDto req
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseMapper.toDto(warehouseService.insertNewWarehouse(req)));
    }

    @PutMapping("{warehouseNumber}")
    public ResponseEntity<ResponseWarehouseDto> updateWarehouse(
            @PathVariable String warehouseNumber,
            @RequestBody @Valid UpdateWarehouseDto req
    ){
        return ResponseEntity.ok(warehouseMapper.toDto(warehouseService.updateWarehouse(warehouseNumber, req)));
    }
}
