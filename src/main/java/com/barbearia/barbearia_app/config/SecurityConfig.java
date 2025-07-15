package com.barbearia.barbearia_app.config;

import com.barbearia.barbearia_app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UsuarioService usuarioService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Endpoints para clientes
                        .requestMatchers("/api/clientes/me/**").hasRole("CLIENTE")
                        .requestMatchers("/api/agendamentos/cliente/**").hasRole("CLIENTE")

                        // Endpoints para barbeiros
                        .requestMatchers("/api/barbeiros/me/**").hasRole("BARBEIRO")
                        .requestMatchers("/api/agendamentos/barbeiro/**").hasRole("BARBEIRO")
                        .requestMatchers("/api/comissoes/barbeiro/**").hasRole("BARBEIRO")
                        .requestMatchers("/api/relatorios/barbeiro/**").hasRole("BARBEIRO")

                        // Endpoints para administradores
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/relatorios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/comissoes/**").hasRole("ADMINISTRADOR")

                        // Endpoints gerais (requerem autenticação)
                        .requestMatchers("/api/servicos/**").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers("/api/barbeiros/**").hasAnyRole("BARBEIRO", "ADMINISTRADOR")
                        .requestMatchers("/api/clientes/**").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers("/api/agendamentos/**").hasAnyRole("CLIENTE", "BARBEIRO", "ADMINISTRADOR")
                        .requestMatchers("/api/horarios-funcionamento/**").hasAnyRole("BARBEIRO", "ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}