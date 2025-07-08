package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.ClienteDTO;
import com.barbearia.barbearia_app.model.Cliente;
import com.barbearia.barbearia_app.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.criar(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
        Cliente cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/telefone/{telefone}")
    public ResponseEntity<Cliente> buscarPorTelefone(@PathVariable String telefone) {
        Cliente cliente = clienteService.buscarPorTelefone(telefone);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Cliente>> buscarPorNome(@PathVariable String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.atualizar(id, clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar-email")
    public ResponseEntity<Boolean> verificarEmailDisponivel(@RequestParam String email) {
        boolean disponivel = clienteService.verificarEmailDisponivel(email);
        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/verificar-telefone")
    public ResponseEntity<Boolean> verificarTelefoneDisponivel(@RequestParam String telefone) {
        boolean disponivel = clienteService.verificarTelefoneDisponivel(telefone);
        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/estatisticas/total")
    public ResponseEntity<Long> contarClientesTotal() {
        Long count = clienteService.contarClientesTotal();
        return ResponseEntity.ok(count);
    }
}