package group.australia.warehouse_management_system.unit.user;

import group.australia.warehouse_management_system.feature.user.controller.UserController;
import group.australia.warehouse_management_system.feature.user.domain.Role;
import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.dto.ResponseUserDto;
import group.australia.warehouse_management_system.feature.user.dto.UpdateUserDto;
import group.australia.warehouse_management_system.feature.user.mapper.UserMapper;
import group.australia.warehouse_management_system.feature.user.service.UserService;
import group.australia.warehouse_management_system.common.security.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private User userDomain;
    private ResponseUserDto dto;
    private ResponseUserDto updateResDto;

    @BeforeEach
    void setUp() {
        userDomain = User.builder()
                .id(1L)
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("hashed-secret")
                .active(true)
                .role(Role.USER)
                .build();

        dto = new ResponseUserDto(1L, "preacher@gmail.com", "Preacher Y", true, Role.USER);
        updateResDto = new ResponseUserDto(1L, "preacher@gmail.com", "Preacher Y Updated", false, Role.USER);
    }

    @Test
    void Should_return_201_when_creating_user() throws Exception {
        var requestDto = new CreateUserDto("preacher@gmail.com", "Preacher Y", "secret");

        given(userService.create(ArgumentMatchers.any())).willReturn(userDomain);
        given(userMapper.toDto(ArgumentMatchers.any(User.class))).willReturn(dto);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void Should_return_200_when_updating_user() throws Exception {
        var updateDto = new UpdateUserDto("Preacher Y Updated", false);

        given(userService.update(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(UpdateUserDto.class)
        )).willReturn(userDomain);
        given(userMapper.toDto(ArgumentMatchers.any(User.class))).willReturn(updateResDto);

        ResultActions response = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void Should_return_200_when_getting_all_users() throws Exception {
        given(userService.getAll()).willReturn(List.of(userDomain));
        given(userMapper.toDto(ArgumentMatchers.any(User.class))).willReturn(dto);

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void Should_return_200_when_getting_user_by_id() throws Exception {
        given(userService.getById(ArgumentMatchers.anyLong())).willReturn(userDomain);
        given(userMapper.toDto(ArgumentMatchers.any(User.class))).willReturn(dto);

        ResultActions response = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
