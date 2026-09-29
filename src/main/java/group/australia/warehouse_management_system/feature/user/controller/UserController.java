package group.australia.warehouse_management_system.feature.user.controller;

import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.dto.ResponseUserDto;
import group.australia.warehouse_management_system.feature.user.dto.UpdateUserDto;
import group.australia.warehouse_management_system.feature.user.mapper.UserMapper;
import group.australia.warehouse_management_system.feature.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody @Valid CreateUserDto req){
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(userService.create(req)));
    }

    @PutMapping("{email}")
    public ResponseEntity<ResponseUserDto> updateUser(
            @PathVariable String email,
            @RequestBody @Valid UpdateUserDto req
    ){
        return ResponseEntity.ok(userMapper.toDto(userService.update(email, req)));
    }

    @GetMapping
    public ResponseEntity<?> getAllUser(){

        var users = userService.getAll().stream().map(userMapper::toDto).toList();

        if (users.isEmpty()){
            return ResponseEntity.ok("Users are Empty !!");
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public  ResponseEntity<ResponseUserDto> getUser(@PathVariable String email){
        return ResponseEntity.ok(userMapper.toDto(userService.getByEmail(email)));
    }
}
