package group.australia.warehouse_management_system.auth;

import group.australia.warehouse_management_system.common.security.JwtService;
import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LogoutRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.RefreshRequestDto;
import group.australia.warehouse_management_system.feature.auth.service.AuthService;
import group.australia.warehouse_management_system.feature.auth.service.RefreshTokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private RefreshTokenService refreshTokenService;

    private AuthService authService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authenticationManager, jwtService, userDetailsService, refreshTokenService);
        userDetails = new User("preacher@gmail.com", "hashed-secret", List.of());
    }

    @Test
    void Should_return_access_and_refresh_tokens_on_successful_login() {
        var request = new LoginRequestDto("preacher@gmail.com", "secret");
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of());

        when(authenticationManager.authenticate(any())).thenReturn(authToken);
        when(jwtService.generateAccessToken(any(UserDetails.class))).thenReturn("access-token");
        when(refreshTokenService.create(anyString())).thenReturn("refresh-token");

        var result = authService.login(request);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.accessToken()).isEqualTo("access-token");
        Assertions.assertThat(result.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void Should_throw_when_credentials_are_invalid() {
        var request = new LoginRequestDto("preacher@gmail.com", "wrong-password");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        Assertions.assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void Should_return_new_tokens_on_valid_refresh() {
        var request = new RefreshRequestDto("valid-refresh-token");
        var rotated = new RefreshTokenService.RotationResult("new-refresh-token", "preacher@gmail.com");

        when(refreshTokenService.useAndRotate(anyString())).thenReturn(rotated);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.generateAccessToken(any(UserDetails.class))).thenReturn("new-access-token");

        var result = authService.refresh(request);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.accessToken()).isEqualTo("new-access-token");
        Assertions.assertThat(result.refreshToken()).isEqualTo("new-refresh-token");
    }

    @Test
    void Should_throw_when_refresh_token_is_expired_or_invalid() {
        var request = new RefreshRequestDto("expired-token");

        when(refreshTokenService.useAndRotate(anyString()))
                .thenThrow(new BadCredentialsException("Refresh token expired"));

        Assertions.assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void Should_throw_on_refresh_token_reuse() {
        var request = new RefreshRequestDto("stolen-token");

        when(refreshTokenService.useAndRotate(anyString()))
                .thenThrow(new BadCredentialsException("Refresh token reuse detected — all sessions revoked"));

        Assertions.assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("reuse");
    }

    @Test
    void Should_revoke_token_on_logout() {
        var request = new LogoutRequestDto("valid-refresh-token");

        doNothing().when(refreshTokenService).revoke(anyString());

        Assertions.assertThatCode(() -> authService.logout(request)).doesNotThrowAnyException();
    }
}
