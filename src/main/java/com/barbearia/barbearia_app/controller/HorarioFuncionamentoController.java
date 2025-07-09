package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.HorarioFuncionamentoRequestDTO;
import com.barbearia.barbearia_app.dto.HorarioFuncionamentoResponseDTO;
import com.barbearia.barbearia_app.service.HorarioFuncionamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/horarios-funcionamento")
@RequiredArgsConstructor
@Tag(name = "Horários de Funcionamento", description = "Gerenciamento de horários de funcionamento dos barbeiros")
@SecurityRequirement(name = "bearerAuth")
public class HorarioFuncionamentoController {

    private final HorarioFuncionamentoService horarioFuncionamentoService;

    @PostMapping
    @Operation(summary = "Criar horário de funcionamento", description = "Cria um novo horário de funcionamento")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<HorarioFuncionamentoResponseDTO> criar(@Valid @RequestBody HorarioFuncionamentoRequestDTO request) {
        HorarioFuncionamentoResponseDTO horario = horarioFuncionamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(horario);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    @Operation(summary = "Buscar horários por barbeiro", description = "Busca horários de funcionamento de um barbeiro")
    public ResponseEntity<List<HorarioFuncionamentoResponseDTO>> buscarPorBarbeiro(@PathVariable Long barbeiroId) {
        List<HorarioFuncionamentoResponseDTO> horarios = horarioFuncionamentoService.buscarPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/dia/{diaSemana}/barbeiros")
    @Operation(summary = "Buscar barbeiros que trabalham em um dia", description = "Busca barbeiros que trabalham em um dia específico")
    public ResponseEntity<List<Long>> buscarBarbeirosQueTrabalhamEm(@PathVariable DayOfWeek diaSemana) {
        List<Long> barbeirosIds = horarioFuncionamentoService.buscarBarbeirosQueTrabalhamEm(diaSemana);
        return ResponseEntity.ok(barbeirosIds);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar horário de funcionamento", description = "Atualiza um horário de funcionamento")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<HorarioFuncionamentoResponseDTO> atualizar(@PathVariable Long id,
                                                                     @Valid @RequestBody HorarioFuncionamentoRequestDTO request) {
        HorarioFuncionamentoResponseDTO horario = horarioFuncionamentoService.atualizar(id, request);
        return ResponseEntity.ok(horario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar horário de funcionamento", description = "Desativa um horário de funcionamento")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        horarioFuncionamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    @Operation(summary = "Ativar horário de funcionamento", description = "Ativa um horário de funcionamento")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        horarioFuncionamentoService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/barbeiro/{barbeiroId}/padrao")
    @Operation(summary = "Criar horários padrão", description = "Cria horários padrão para um barbeiro")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> criarHorariosPadrao(@PathVariable Long barbeiroId) {
        horarioFuncionamentoService.criarHorariosPadrao(barbeiroId);
        return ResponseEntity.noContent().build();
    }
}