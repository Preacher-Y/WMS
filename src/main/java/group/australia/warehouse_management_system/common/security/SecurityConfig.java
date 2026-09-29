package group.australia.warehouse_management_system.common.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth

                // ======== PUBLIC ========
                // .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                // .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                // .requestMatchers(HttpMethod.POST, "/users").permitAll()

                // ======== ADMIN — master data ========
                // .requestMatchers(HttpMethod.POST,  "/warehouses").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.PUT,   "/warehouses/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.POST,  "/warehouses/*/locations").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.POST,  "/carton-headers").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.PUT,   "/carton-headers/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.POST,  "/carton-headers/*/ssccs").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.GET,   "/users").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.PUT,   "/users/**").hasRole("ADMIN")

                // ======== USER — operational ========
                // .requestMatchers(HttpMethod.POST, "/items").hasRole("USER")
                // .requestMatchers(HttpMethod.GET,  "/users/**").authenticated()

                // ======== DEFAULT ========
                // .anyRequest().authenticated()
                .anyRequest().permitAll()   // temporary: all public during development
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
