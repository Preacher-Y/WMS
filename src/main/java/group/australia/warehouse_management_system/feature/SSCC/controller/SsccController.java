package group.australia.warehouse_management_system.feature.SSCC.controller;

import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.service.SsccService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carton-headers")
@RequiredArgsConstructor
public class SsccController {
    private final SsccService ssccService;

    @PostMapping("/{barcode}/ssccs")
    public ResponseEntity<ResponseSsccDto> createNewSscc(
            @PathVariable String barcode,
            @RequestBody @Valid CreateSsccDto req
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(ssccService.insertNewSscc(barcode,req));
    }
}
