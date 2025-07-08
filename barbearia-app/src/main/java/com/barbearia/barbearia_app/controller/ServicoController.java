package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.ServicoDTO;
import com.barbearia.barbearia_app.model.Servico;
import com.barbearia.barbearia_app.service.ServicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
@Tag(name = "Serviços")
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public ResponseEntity<Servico> criar(@Valid @RequestBody ServicoDTO servicoDTO) {
        Servico servico = servicoService.criar(servicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listarTodos() {
        List<Servico> servicos = servicoService.listarTodos();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Servico>> listarAtivos() {
        List<Servico> servicos = servicoService.listarAtivos();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
        Servico servico = servicoService.buscarPorId(id);
        return ResponseEntity.ok(servico);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Servico> buscarPorNome(@PathVariable String nome) {
        Servico servico = servicoService.buscarPorNome(nome);
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @Valid @RequestBody ServicoDTO servicoDTO) {
        Servico servico = servicoService.atualizar(id, servicoDTO);
        return ResponseEntity.ok(servico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Servico> ativar(@PathVariable Long id) {
        Servico servico = servicoService.ativar(id);
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<Servico> desativar(@PathVariable Long id) {
        Servico servico = servicoService.desativar(id);
        return ResponseEntity.ok(servico);
    }

    @GetMapping("/preco")
    public ResponseEntity<List<Servico>> buscarPorFaixaPreco(
            @RequestParam BigDecimal precoMin,  // ✅ Usar BigDecimal
            @RequestParam BigDecimal precoMax) {
        List<Servico> servicos = servicoService.buscarPorFaixaPreco(precoMin, precoMax);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/duracao")
    public ResponseEntity<List<Servico>> buscarPorDuracao(
            @RequestParam Integer duracaoMin,
            @RequestParam Integer duracaoMax) {
        List<Servico> servicos = servicoService.buscarPorDuracao(duracaoMin, duracaoMax);
        return ResponseEntity.ok(servicos);
    }
}