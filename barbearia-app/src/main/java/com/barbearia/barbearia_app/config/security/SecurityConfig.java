package com.barbearia.barbearia_app.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos (para clientes com token simples)
                        .requestMatchers("/api/auth/token").permitAll()
                        .requestMatchers("/api/servicos").permitAll()
                        .requestMatchers("/api/barbeiros").permitAll()
                        .requestMatchers("/api/agendamentos/horarios-disponiveis").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/agendamentos").permitAll() // Criar agendamento
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/cliente/**").permitAll() // Ver próprios agendamentos
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Endpoints apenas para administradores
                        .requestMatchers("/api/auth/admin/login").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/servicos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/barbeiros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/barbeiros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/barbeiros/**").hasRole("ADMIN")
                        .requestMatchers("/api/agendamentos").hasRole("ADMIN") // Listar todos os agendamentos
                        .requestMatchers(HttpMethod.PUT, "/api/agendamentos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agendamentos/**").hasRole("ADMIN")
                        .requestMatchers("/api/agendamentos/**/confirmar").hasRole("ADMIN")
                        .requestMatchers("/api/agendamentos/**/concluir").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}