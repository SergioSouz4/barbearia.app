package com.barbearia.config;

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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/servicos").permitAll()
                        .requestMatchers("/api/barbeiros").permitAll()

                        // Endpoints protegidos
                        .requestMatchers("/api/clientes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/agendamentos/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/servicos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/barbeiros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/barbeiros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/barbeiros/**").hasRole("ADMIN")
