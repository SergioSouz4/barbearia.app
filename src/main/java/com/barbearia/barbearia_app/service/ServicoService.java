package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.ServicoRequestDTO;
import com.barbearia.barbearia_app.dto.ServicoResponseDTO;
import com.barbearia.barbearia_app.dto.ServicoResumoDTO;
import com.barbearia.barbearia_app.model.Servico;
import com.barbearia.barbearia_app.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    @Transactional
    public ServicoResponseDTO criar(ServicoRequestDTO request) {
        // Verificar se já existe serviço com o mesmo nome
        if (servicoRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new RuntimeException("Já existe um serviço com este nome");
        }

        Servico servico = new Servico();
        servico.setNome(request.getNome());
        servico.setDescricao(request.getDescricao());
        servico.setPreco(request.getPreco());
        servico.setDuracaoMinutos(request.getDuracaoMinutos());

        Servico servicoSalvo = servicoRepository.save(servico);
        return converterParaResponseDTO(servicoSalvo);
    }

    public List<ServicoResponseDTO> listarTodos() {
        return servicoRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ServicoResumoDTO> listarResumo() {
        return servicoRepository.findByAtivoTrueOrderByNome()
                .stream()
                .map(this::converterParaResumoDTO)
                .collect(Collectors.toList());
    }

    public ServicoResponseDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        return converterParaResponseDTO(servico);
    }

    public List<ServicoResponseDTO> buscarPorNome(String nome) {
        return servicoRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServicoResponseDTO atualizar(Long id, ServicoRequestDTO request) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Verificar se já existe outro serviço com o mesmo nome
        if (!servico.getNome().equalsIgnoreCase(request.getNome()) &&
                servicoRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new RuntimeException("Já existe um serviço com este nome");
        }

        servico.setNome(request.getNome());
        servico.setDescricao(request.getDescricao());
        servico.setPreco(request.getPreco());
        servico.setDuracaoMinutos(request.getDuracaoMinutos());
        servico.setDataAtualizacao(LocalDateTime.now());

        Servico servicoAtualizado = servicoRepository.save(servico);
        return converterParaResponseDTO(servicoAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Soft delete - apenas desativa o serviço
        servico.setAtivo(false);
        servico.setDataAtualizacao(LocalDateTime.now());
        servicoRepository.save(servico);
    }

    @Transactional
    public void ativar(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        servico.setAtivo(true);
        servico.setDataAtualizacao(LocalDateTime.now());
        servicoRepository.save(servico);
    }

    public Long contarTotal() {
        return servicoRepository.count();
    }

    public Long contarAtivos() {
        return servicoRepository.countByAtivoTrue();
    }

    // Métodos de conversão
    private ServicoResponseDTO converterParaResponseDTO(Servico servico) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracaoMinutos(),
                servico.isAtivo(),
                servico.getDataCriacao(),
                servico.getDataAtualizacao()
        );
    }

    private ServicoResumoDTO converterParaResumoDTO(Servico servico) {
        return new ServicoResumoDTO(
                servico.getId(),
                servico.getNome(),
                servico.getPreco(),
                servico.getDuracaoMinutos()
        );
    }
}