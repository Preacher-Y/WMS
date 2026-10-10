package group.australia.warehouse_management_system.feature.auth.service;

import group.australia.warehouse_management_system.common.security.JwtService;
import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LoginResponseDto;
import group.australia.warehouse_management_system.feature.auth.dto.LogoutRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.RefreshRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDto login(LoginRequestDto req) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        var userDetails = (UserDetails) auth.getPrincipal();
        return new LoginResponseDto(
                jwtService.generateAccessToken(userDetails),
                refreshTokenService.create(userDetails.getUsername())
        );
    }

    public LoginResponseDto refresh(RefreshRequestDto req) {
        var rotated = refreshTokenService.useAndRotate(req.refreshToken());
        var userDetails = userDetailsService.loadUserByUsername(rotated.userEmail());
        return new LoginResponseDto(
                jwtService.generateAccessToken(userDetails),
                rotated.newToken()
        );
    }

    public void logout(LogoutRequestDto req) {
        refreshTokenService.revoke(req.refreshToken());
    }
}
