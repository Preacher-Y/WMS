package group.australia.warehouse_management_system.feature.location.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import group.australia.warehouse_management_system.feature.location.dto.LocationRequestDto;
import group.australia.warehouse_management_system.feature.location.dto.LocationResponseDto;
import group.australia.warehouse_management_system.feature.location.service.LocationService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/warehouses/{warehouseId}/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponseDto> createLocation(
            @PathVariable Long warehouseId,
            @Valid @RequestBody LocationRequestDto request) {

        LocationResponseDto response = locationService.insertNewLocation(warehouseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
