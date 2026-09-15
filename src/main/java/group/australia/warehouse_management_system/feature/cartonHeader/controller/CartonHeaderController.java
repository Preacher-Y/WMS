package group.australia.warehouse_management_system.feature.cartonHeader.controller;

import group.australia.warehouse_management_system.feature.cartonHeader.dto.RequestCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
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

    @PostMapping
    public ResponseEntity<ResponseCartonHeaderDto> createCartonHeader(
            @RequestBody @Valid RequestCartonHeaderDto req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartonHeaderService.insertNewCartonHeader(req));
    }

    @PutMapping("{barcode}")
    public  ResponseEntity<ResponseCartonHeaderDto> updateCartonHeader(
            @PathVariable String barcode,
            @RequestBody @Valid UpdateCartonHeaderDto req
    ) {
        return ResponseEntity.ok(cartonHeaderService.updateCartonHeader(barcode, req));
    }
}
