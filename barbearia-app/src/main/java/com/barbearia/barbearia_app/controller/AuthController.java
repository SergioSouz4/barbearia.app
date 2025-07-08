package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.config.security.JwtTokenProvider;
import com.barbearia.barbearia_app.dto.AdminLoginRequestDTO;
import com.barbearia.barbearia_app.dto.SimpleTokenResponseDTO;
import com.barbearia.barbearia_app.dto.TokenRequestDTO;
import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.service.UsuarioService;
import com.barbearia.barbearia_app.service.CaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;
    private final CaptchaService captchaService;

    @PostMapping("/token")
    public ResponseEntity<?> gerarTokenCliente(@Valid @RequestBody TokenRequestDTO tokenRequest) {
        if (!captchaService.validarCaptcha(tokenRequest.getCaptchaToken())) {
            return ResponseEntity.badRequest().body("Token anti-bot inválido");
        }

        String jwt = tokenProvider.generateSimpleToken(tokenRequest.getNome(), "ROLE_CLIENT");
        return ResponseEntity.ok(new SimpleTokenResponseDTO(jwt, tokenRequest.getNome(), "CLIENT"));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody AdminLoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getSenha()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Usuario usuario = usuarioService.buscarPorEmail(loginRequest.getEmail());
            return ResponseEntity.ok(new SimpleTokenResponseDTO(jwt, usuario.getNome(), "ADMIN"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }
}