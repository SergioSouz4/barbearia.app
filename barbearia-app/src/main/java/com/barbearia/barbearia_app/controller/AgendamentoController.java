package com.barbearia.controller;

import com.barbearia.dto.AgendamentoDTO;
import com.barbearia.model.Agendamento;
import com.barbearia.model.StatusAgendamento;
import com.barbearia.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Agendamento>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorCliente(clienteId));
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<Agendamento>> listarPorBarbeiro(@PathVariable Long barbeiroId) {
        return ResponseEntity.ok(agendamentoService.listarPorBarbeiro(barbeiroId));
    }

    @GetMapping("/futuros")
    public ResponseEntity<List<Agendamento>> listarAgendamentosFuturos() {
        return ResponseEntity.ok(agendamentoService.listarAgendamentosFuturos());
    }

    @PostMapping
    public ResponseEntity<Agendamento> criar(@Valid @RequestBody AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = agendamentoService.criar(agendamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoDTO agendamentoDTO) {
        return ResponseEntity.ok(agendamentoService.atualizar(id, agendamentoDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Agendamento> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.alterarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}