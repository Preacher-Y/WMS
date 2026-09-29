package group.australia.warehouse_management_system.auth;

import group.australia.warehouse_management_system.feature.auth.controller.AuthController;
import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LoginResponseDto;
import group.australia.warehouse_management_system.feature.auth.dto.LogoutRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.RefreshRequestDto;
import group.australia.warehouse_management_system.feature.auth.service.AuthService;
import group.australia.warehouse_management_system.common.security.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void Should_return_200_with_tokens_when_credentials_are_valid() throws Exception {
        var request = new LoginRequestDto("preacher@gmail.com", "secret");
        var response = new LoginResponseDto("access-token", "refresh-token");

        given(authService.login(ArgumentMatchers.any(LoginRequestDto.class))).willReturn(response);

        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void Should_return_401_when_credentials_are_invalid() throws Exception {
        var request = new LoginRequestDto("preacher@gmail.com", "wrong-password");

        given(authService.login(ArgumentMatchers.any(LoginRequestDto.class)))
                .willThrow(new BadCredentialsException("Bad credentials"));

        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void Should_return_200_with_new_tokens_when_refresh_token_is_valid() throws Exception {
        var request = new RefreshRequestDto("valid-refresh-token");
        var response = new LoginResponseDto("new-access-token", "new-refresh-token");

        given(authService.refresh(ArgumentMatchers.any(RefreshRequestDto.class))).willReturn(response);

        ResultActions result = mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void Should_return_401_when_refresh_token_is_invalid() throws Exception {
        var request = new RefreshRequestDto("expired-token");

        given(authService.refresh(ArgumentMatchers.any(RefreshRequestDto.class)))
                .willThrow(new BadCredentialsException("Invalid refresh token"));

        ResultActions result = mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void Should_return_204_when_logout_is_successful() throws Exception {
        var request = new LogoutRequestDto("valid-refresh-token");

        willDoNothing().given(authService).logout(ArgumentMatchers.any(LogoutRequestDto.class));

        ResultActions result = mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
