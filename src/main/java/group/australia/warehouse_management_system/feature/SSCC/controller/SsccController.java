package group.australia.warehouse_management_system.feature.SSCC.controller;

import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
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
    private final SsccMapper ssccMapper;

    @PostMapping("/{barcode}/ssccs")
    public ResponseEntity<ResponseSsccDto> createNewSscc(
            @PathVariable String barcode,
            @RequestBody @Valid CreateSsccDto req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ssccMapper.toDto(ssccService.insertNewSscc(barcode, req)));
    }
}
