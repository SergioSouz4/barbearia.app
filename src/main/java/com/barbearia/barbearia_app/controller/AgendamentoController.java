package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.*;
import com.barbearia.barbearia_app.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos")
@SecurityRequirement(name = "bearerAuth")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO request) {
        AgendamentoResponseDTO agendamento = agendamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
    }

    @GetMapping
    @Operation(summary = "Listar todos os agendamentos", description = "Lista todos os agendamentos (apenas administradores)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodos() {
        List<AgendamentoResponseDTO> agendamentos = agendamentoService.listarTodos();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar agendamentos por cliente", description = "Busca agendamentos de um cliente específico")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        List<AgendamentoResponseDTO> agendamentos = agendamentoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    @Operation(summary = "Buscar agendamentos por barbeiro", description = "Busca agendamentos de um barbeiro específico")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorBarbeiro(@PathVariable Long barbeiroId) {
        List<AgendamentoResponseDTO> agendamentos = agendamentoService.buscarPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/barbeiro/{barbeiroId}/data/{data}")
    @Operation(summary = "Buscar agendamentos por barbeiro e data", description = "Busca agendamentos de um barbeiro em uma data específica")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorBarbeiroEData(
            @PathVariable Long barbeiroId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResponseDTO> agendamentos = agendamentoService.buscarPorBarbeiroEData(barbeiroId, data);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/cliente/{clienteId}/proximos")
    @Operation(summary = "Buscar próximos agendamentos do cliente", description = "Busca próximos agendamentos de um cliente")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarProximosCliente(@PathVariable Long clienteId) {
        List<AgendamentoResponseDTO> agendamentos = agendamentoService.buscarProximosCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Busca um agendamento específico")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        AgendamentoResponseDTO agendamento = agendamentoService.buscarPorId(id);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento", description = "Confirma um agendamento")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<AgendamentoResponseDTO> confirmar(@PathVariable Long id) {
        AgendamentoResponseDTO agendamento = agendamentoService.confirmar(id);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/realizar")
    @Operation(summary = "Realizar agendamento", description = "Marca um agendamento como realizado")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<AgendamentoResponseDTO> realizar(@PathVariable Long id) {
        AgendamentoResponseDTO agendamento = agendamentoService.realizar(id);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento")
    @PreAuthorize("hasAnyRole('CLIENTE', 'BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(@PathVariable Long id,
                                                           @RequestParam(required = false) String motivo) {
        AgendamentoResponseDTO agendamento = agendamentoService.cancelar(id, motivo);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/nao-compareceu")
    @Operation(summary = "Marcar como não compareceu", description = "Marca agendamento como não compareceu")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<AgendamentoResponseDTO> marcarNaoCompareceu(@PathVariable Long id) {
        AgendamentoResponseDTO agendamento = agendamentoService.marcarNaoCompareceu(id);
        return ResponseEntity.ok(agendamento);
    }

    @PostMapping("/disponibilidade")
    @Operation(summary = "Buscar horários disponíveis", description = "Busca horários disponíveis para agendamento")
    public ResponseEntity<List<HorarioDisponivelDTO>> buscarHorariosDisponiveis(
            @Valid @RequestBody DisponibilidadeRequestDTO request) {
        List<HorarioDisponivelDTO> horarios = agendamentoService.buscarHorariosDisponiveis(request);
        return ResponseEntity.ok(horarios);
    }
}