package group.australia.warehouse_management_system.feature.location.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.dto.CreateLocationDto;
import group.australia.warehouse_management_system.feature.location.dto.LocationResponseDto;
import group.australia.warehouse_management_system.feature.location.mapper.LocationMapper;
import group.australia.warehouse_management_system.feature.location.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/warehouses/{warehouseId}/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @PostMapping
    public ResponseEntity<LocationResponseDto> createLocation(
            @PathVariable Long warehouseId,
            @Valid @RequestBody CreateLocationDto request) {

        Location location = locationMapper.toDomain(request);
        Location saved = locationService.insertNewLocation(warehouseId, location);
        LocationResponseDto response = locationMapper.toResponseDto(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}