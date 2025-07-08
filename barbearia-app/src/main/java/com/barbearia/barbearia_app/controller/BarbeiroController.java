package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.BarbeiroDTO;
import com.barbearia.barbearia_app.model.Barbeiro;
import com.barbearia.barbearia_app.service.BarbeiroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbeiros")
@RequiredArgsConstructor
@Tag(name = "Barbeiros")
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    @PostMapping
    public ResponseEntity<Barbeiro> criar(@Valid @RequestBody BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = barbeiroService.criar(barbeiroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(barbeiro);
    }

    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarTodos() {
        List<Barbeiro> barbeiros = barbeiroService.listarTodos();
        return ResponseEntity.ok(barbeiros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barbeiro> buscarPorId(@PathVariable Long id) {
        Barbeiro barbeiro = barbeiroService.buscarPorId(id);
        return ResponseEntity.ok(barbeiro);
    }

    @GetMapping("/telefone/{telefone}")
    public ResponseEntity<Barbeiro> buscarPorTelefone(@PathVariable String telefone) {
        Barbeiro barbeiro = barbeiroService.buscarPorTelefone(telefone);
        return ResponseEntity.ok(barbeiro);
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<Barbeiro>> buscarPorEspecialidade(@PathVariable String especialidade) {
        List<Barbeiro> barbeiros = barbeiroService.buscarPorEspecialidade(especialidade);
        return ResponseEntity.ok(barbeiros);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Barbeiro>> buscarPorNome(@PathVariable String nome) {
        List<Barbeiro> barbeiros = barbeiroService.buscarPorNome(nome);
        return ResponseEntity.ok(barbeiros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barbeiro> atualizar(@PathVariable Long id, @Valid @RequestBody BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = barbeiroService.atualizar(id, barbeiroDTO);
        return ResponseEntity.ok(barbeiro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}