package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.ClienteRequestDTO;
import com.barbearia.barbearia_app.dto.ClienteResponseDTO;
import com.barbearia.barbearia_app.dto.ClienteResumoDTO;
import com.barbearia.barbearia_app.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cria um novo cliente (apenas administradores)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO cliente = clienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Lista todos os clientes (apenas administradores)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/resumo")
    @Operation(summary = "Listar resumo dos clientes", description = "Lista resumo dos clientes ativos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ClienteResumoDTO>> listarResumo() {
        List<ClienteResumoDTO> clientes = clienteService.listarResumo();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Busca um cliente específico")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar clientes por nome", description = "Busca clientes pelo nome")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@PathVariable String nome) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza dados de um cliente")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO cliente = clienteService.atualizar(id, request);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar cliente", description = "Desativa um cliente")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    @Operation(summary = "Ativar cliente", description = "Ativa um cliente desativado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        clienteService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estatisticas/total")
    @Operation(summary = "Estatísticas de clientes", description = "Retorna estatísticas dos clientes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        Map<String, Object> stats = Map.of(
                "total", clienteService.contarTotal(),
                "ativos", clienteService.contarAtivos()
        );
        return ResponseEntity.ok(stats);
    }
}