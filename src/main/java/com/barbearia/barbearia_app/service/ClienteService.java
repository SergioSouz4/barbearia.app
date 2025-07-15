package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.*;
import com.barbearia.barbearia_app.model.Cliente;
import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.model.enums.TipoUsuario;
import com.barbearia.barbearia_app.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO request) {
        // Verificar se email já existe
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Verificar se telefone já existe
        if (clienteRepository.existsByTelefone(request.getTelefone())) {
            throw new RuntimeException("Telefone já cadastrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());
        cliente.setDataNascimento(request.getDataNascimento());

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return converterParaResponseDTO(clienteSalvo);
    }

    @Transactional
    public ClienteResponseDTO criarComUsuario(ClienteRequestDTO request, Usuario usuario) {
        // Verificar se email já existe
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Verificar se telefone já existe
        if (clienteRepository.existsByTelefone(request.getTelefone())) {
            throw new RuntimeException("Telefone já cadastrado");
        }

        // Salvar usuário
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        // Criar cliente
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());
        cliente.setDataNascimento(request.getDataNascimento());
        cliente.setUsuario(usuarioSalvo);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return converterParaResponseDTO(clienteSalvo);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteResumoDTO> listarResumo() {
        return clienteRepository.findByAtivoTrueOrderByNome()
                .stream()
                .map(this::converterParaResumoDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return converterParaResponseDTO(cliente);
    }

    public Cliente buscarPorUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId)
                .orElse(null);
    }

    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verificar se email já existe (exceto o próprio cliente)
        if (!cliente.getEmail().equals(request.getEmail()) &&
                clienteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Verificar se telefone já existe (exceto o próprio cliente)
        if (!cliente.getTelefone().equals(request.getTelefone()) &&
                clienteRepository.existsByTelefone(request.getTelefone())) {
            throw new RuntimeException("Telefone já cadastrado");
        }

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());
        cliente.setDataNascimento(request.getDataNascimento());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return converterParaResponseDTO(clienteAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Soft delete - apenas desativa o cliente
        cliente.setAtivo(false);
        clienteRepository.save(cliente);

        // Desativar usuário também
        if (cliente.getUsuario() != null) {
            usuarioService.desativarUsuario(cliente.getUsuario().getId());
        }
    }

    @Transactional
    public void ativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setAtivo(true);
        clienteRepository.save(cliente);

        // Ativar usuário também
        if (cliente.getUsuario() != null) {
            usuarioService.ativarUsuario(cliente.getUsuario().getId());
        }
    }

    public Long contarTotal() {
        return clienteRepository.count();
    }

    public Long contarAtivos() {
        return clienteRepository.countByAtivoTrue();
    }

    // Métodos de conversão
    private ClienteResponseDTO converterParaResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setDataCadastro(cliente.getDataCadastro());
        dto.setAtivo(cliente.isAtivo());

        if (cliente.getUsuario() != null) {
            dto.setUsuarioId(cliente.getUsuario().getId());
        }

        // Calcular estatísticas (se necessário)
        if (cliente.getAgendamentos() != null) {
            dto.setTotalAgendamentos(cliente.getAgendamentos().size());

            cliente.getAgendamentos().stream()
                    .map(agendamento -> agendamento.getDataHora())
                    .max(LocalDateTime::compareTo)
                    .ifPresent(dto::setUltimoAgendamento);
        }

        return dto;
    }

    private ClienteResumoDTO converterParaResumoDTO(Cliente cliente) {
        ClienteResumoDTO dto = new ClienteResumoDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setEmail(cliente.getEmail());
        dto.setAtivo(cliente.isAtivo());

        // Calcular estatísticas básicas
        if (cliente.getAgendamentos() != null) {
            dto.setTotalAgendamentos(cliente.getAgendamentos().size());

            cliente.getAgendamentos().stream()
                    .map(agendamento -> agendamento.getDataHora())
                    .max(LocalDateTime::compareTo)
                    .ifPresent(dto::setUltimoAgendamento);
        }

        return dto;
    }
}