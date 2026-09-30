package group.australia.warehouse_management_system.feature.recieve.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.recieve.dto.CreateRecieverDto;
import group.australia.warehouse_management_system.feature.recieve.service.ReceiveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receive")
@RequiredArgsConstructor
public class ReceiveController {

    private final ReceiveService service;

    @PostMapping
    public Item recieve(@Valid @RequestBody CreateRecieverDto dto) {
        return service.recieve(dto);
    }
}