package group.australia.warehouse_management_system.feature.auth.controller;

import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LoginResponseDto;
import group.australia.warehouse_management_system.feature.auth.dto.RefreshRequestDto;
import group.australia.warehouse_management_system.feature.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody @Valid RefreshRequestDto req) {
        return ResponseEntity.ok(authService.refresh(req));
    }
}
