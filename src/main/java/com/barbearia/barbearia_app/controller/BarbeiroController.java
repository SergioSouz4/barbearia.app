package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.BarbeiroRequestDTO;
import com.barbearia.barbearia_app.dto.BarbeiroResponseDTO;
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
    public ResponseEntity<BarbeiroResponseDTO> criar(@Valid @RequestBody BarbeiroRequestDTO barbeiroDTO) {
        BarbeiroResponseDTO barbeiro = barbeiroService.criar(barbeiroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(barbeiro);
    }

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        List<BarbeiroResponseDTO> barbeiros = barbeiroService.listarTodos();
        return ResponseEntity.ok(barbeiros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        BarbeiroResponseDTO barbeiro = barbeiroService.buscarPorId(id);
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
    public ResponseEntity<List<BarbeiroResponseDTO>> buscarPorNome(@PathVariable String nome) {
        List<BarbeiroResponseDTO> barbeiros = barbeiroService.buscarPorNome(nome);
        return ResponseEntity.ok(barbeiros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody BarbeiroRequestDTO barbeiroDTO) {
        BarbeiroResponseDTO barbeiro = barbeiroService.atualizar(id, barbeiroDTO);
        return ResponseEntity.ok(barbeiro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}