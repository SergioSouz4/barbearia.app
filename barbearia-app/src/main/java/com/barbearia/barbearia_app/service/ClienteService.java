package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.ClienteDTO;
import com.barbearia.barbearia_app.model.Cliente;
import com.barbearia.barbearia_app.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente criar(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + clienteDTO.getEmail());
        }

        if (clienteRepository.existsByTelefone(clienteDTO.getTelefone())) {
            throw new RuntimeException("Já existe um cliente com este telefone: " + clienteDTO.getTelefone());
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }

    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com email: " + email));
    }

    public Cliente buscarPorTelefone(String telefone) {
        return clienteRepository.findByTelefone(telefone)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com telefone: " + telefone));
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Cliente atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = buscarPorId(id);

        if (!cliente.getEmail().equals(clienteDTO.getEmail()) &&
                clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + clienteDTO.getEmail());
        }

        if (!cliente.getTelefone().equals(clienteDTO.getTelefone()) &&
                clienteRepository.existsByTelefone(clienteDTO.getTelefone())) {
            throw new RuntimeException("Já existe um cliente com este telefone: " + clienteDTO.getTelefone());
        }

        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());

        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    public boolean verificarEmailDisponivel(String email) {
        return !clienteRepository.existsByEmail(email);
    }

    public boolean verificarTelefoneDisponivel(String telefone) {
        return !clienteRepository.existsByTelefone(telefone);
    }

    public Long contarClientesTotal() {
        return clienteRepository.count();
    }
}