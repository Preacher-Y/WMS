package group.australia.warehouse_management_system.feature.cartonHeader.controller;

import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import group.australia.warehouse_management_system.feature.cartonHeader.service.CartonHeaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carton-headers")
@RequiredArgsConstructor
public class CartonHeaderController {

    private final CartonHeaderService cartonHeaderService;
    private final CartonHeaderMapper cartonHeaderMapper;

    @PostMapping
    public ResponseEntity<ResponseCartonHeaderDto> createCartonHeader(
            @RequestBody @Valid CreateCartonHeaderDto req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartonHeaderMapper.toDto(cartonHeaderService.insertNewCartonHeader(req)));
    }

    @PutMapping("{barcode}")
    public ResponseEntity<ResponseCartonHeaderDto> updateCartonHeader(
            @PathVariable String barcode,
            @RequestBody @Valid UpdateCartonHeaderDto req
    ) {
        return ResponseEntity.ok(cartonHeaderMapper.toDto(cartonHeaderService.updateCartonHeader(barcode, req)));
    }
}
