package group.australia.warehouse_management_system.feature.item.controller;

import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ResponseItemDto> createItem(@RequestBody @Valid ItemRequestDto req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.insertNewItem(req));
    }
}
