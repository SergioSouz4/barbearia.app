package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.ServicoRequestDTO;
import com.barbearia.barbearia_app.dto.ServicoResponseDTO;
import com.barbearia.barbearia_app.dto.ServicoResumoDTO;
import com.barbearia.barbearia_app.service.ServicoService;
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
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
@Tag(name = "Serviços", description = "Gerenciamento de serviços da barbearia")
@SecurityRequirement(name = "bearerAuth")
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    @Operation(summary = "Criar serviço", description = "Cria um novo serviço (apenas administradores)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody ServicoRequestDTO request) {
        ServicoResponseDTO servico = servicoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços", description = "Lista todos os serviços (apenas administradores)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {
        List<ServicoResponseDTO> servicos = servicoService.listarTodos();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/resumo")
    @Operation(summary = "Listar resumo dos serviços", description = "Lista resumo dos serviços ativos (público)")
    public ResponseEntity<List<ServicoResumoDTO>> listarResumo() {
        List<ServicoResumoDTO> servicos = servicoService.listarResumo();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID", description = "Busca um serviço específico")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        ServicoResponseDTO servico = servicoService.buscarPorId(id);
        return ResponseEntity.ok(servico);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar serviços por nome", description = "Busca serviços pelo nome")
    public ResponseEntity<List<ServicoResponseDTO>> buscarPorNome(@PathVariable String nome) {
        List<ServicoResponseDTO> servicos = servicoService.buscarPorNome(nome);
        return ResponseEntity.ok(servicos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar serviço", description = "Atualiza dados de um serviço")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ServicoResponseDTO> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ServicoRequestDTO request) {
        ServicoResponseDTO servico = servicoService.atualizar(id, request);
        return ResponseEntity.ok(servico);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar serviço", description = "Desativa um serviço")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    @Operation(summary = "Ativar serviço", description = "Ativa um serviço desativado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        servicoService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estatisticas/total")
    @Operation(summary = "Estatísticas de serviços", description = "Retorna estatísticas dos serviços")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        Map<String, Object> stats = Map.of(
                "total", servicoService.contarTotal(),
                "ativos", servicoService.contarAtivos()
        );
        return ResponseEntity.ok(stats);
    }
}