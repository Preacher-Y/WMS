package group.australia.warehouse_management_system.feature.auth.service;

import group.australia.warehouse_management_system.common.security.JwtService;
import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LoginResponseDto;
import group.australia.warehouse_management_system.feature.auth.dto.RefreshRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public LoginResponseDto login(LoginRequestDto req) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        var userDetails = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();
        return new LoginResponseDto(
                jwtService.generateAccessToken(userDetails),
                jwtService.generateRefreshToken(userDetails)
        );
    }

    public LoginResponseDto refresh(RefreshRequestDto req) {
        var token = req.refreshToken();
        var email = jwtService.extractEmail(token);
        var userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(token, userDetails) || !jwtService.isRefreshToken(token)) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid or expired refresh token");
        }

        return new LoginResponseDto(
                jwtService.generateAccessToken(userDetails),
                jwtService.generateRefreshToken(userDetails)
        );
    }
}
