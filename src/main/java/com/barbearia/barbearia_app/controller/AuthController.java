package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.config.JwtUtil;
import com.barbearia.barbearia_app.dto.*;
import com.barbearia.barbearia_app.model.Cliente;
import com.barbearia.barbearia_app.model.Barbeiro;
import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.model.enums.TipoUsuario;
import com.barbearia.barbearia_app.service.ClienteService;
import com.barbearia.barbearia_app.service.BarbeiroService;
import com.barbearia.barbearia_app.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação e cadastro")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final BarbeiroService barbeiroService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica um usuário e retorna token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // Autenticar usuário
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // Obter detalhes do usuário
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Usuario> usuario = Optional.ofNullable(usuarioService.buscarPorEmail(request.getEmail()));

        // Determinar perfil específico e nome
        Long perfilId = null;
        String nome = usuario.get().getEmail(); // Fallback

        if (usuario.get().getTipo() == TipoUsuario.CLIENTE) {
            Cliente cliente = clienteService.buscarPorUsuarioId(usuario.get().getId());
            if (cliente != null) {
                perfilId = cliente.getId();
                nome = cliente.getNome();
            }
        } else if (usuario.get().getTipo() == TipoUsuario.BARBEIRO) {
            Barbeiro barbeiro = barbeiroService.buscarPorUsuarioId(usuario.get().getId());
            if (barbeiro != null) {
                perfilId = barbeiro.getId();
                nome = barbeiro.getNome();
            }
        } else if (usuario.get().getTipo() == TipoUsuario.ADMINISTRADOR) {
            nome = "Administrador";
        }

        // Gerar token com informações completas
        String token = jwtUtil.generateTokenWithUserInfo(
                userDetails,
                usuario.get().getId(),
                usuario.get().getTipo().name(),
                perfilId,
                nome
        );

        // Criar resposta
        LoginResponseDTO response = new LoginResponseDTO(
                token,
                "Bearer",
                usuario.get().getId(),
                usuario.get().getEmail(),
                usuario.get().getTipo().name(),
                nome,
                perfilId
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro/cliente")
    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema")
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(@Valid @RequestBody CadastroClienteRequestDTO request) {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setTipo(TipoUsuario.CLIENTE);

        // Criar cliente com usuário
        ClienteRequestDTO clienteRequest = new ClienteRequestDTO(
                request.getNome(),
                request.getEmail(),
                request.getTelefone(),
                request.getDataNascimento()
        );

        ClienteResponseDTO cliente = clienteService.criarComUsuario(clienteRequest, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/me")
    @Operation(summary = "Dados do usuário logado", description = "Retorna dados do usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Object>> dadosUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        Map<String, Object> dados = Map.of(
                "id", usuario.getId(),
                "email", usuario.getEmail(),
                "tipo", usuario.getTipo().name(),
                "ativo", usuario.isAtivo()
        );

        return ResponseEntity.ok(dados);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token", description = "Renova um token JWT válido")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.isTokenValid(token)) {
                String newToken = jwtUtil.refreshToken(token);

                Map<String, String> response = Map.of(
                        "token", newToken,
                        "tipo", "Bearer"
                );

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token inválido"));
    }
}