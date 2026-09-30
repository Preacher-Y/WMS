package group.australia.warehouse_management_system.feature.moveItem.controller;

import group.australia.warehouse_management_system.feature.moveItem.dto.RequestMoveItemDto;
import group.australia.warehouse_management_system.feature.moveItem.service.MoveItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/move")
@RequiredArgsConstructor
public class MoveItemController {

    private final MoveItemService moveItemService;

    @PostMapping
    public ResponseEntity<Void> moveItem(@RequestBody @Valid RequestMoveItemDto req) {
        moveItemService.moveItem(req);
        return ResponseEntity.ok().build();
    }
}
