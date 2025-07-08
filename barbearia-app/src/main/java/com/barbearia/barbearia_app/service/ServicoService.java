package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.ServicoDTO;
import com.barbearia.barbearia_app.model.Servico;
import com.barbearia.barbearia_app.repository.ServicoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public Servico criar(ServicoDTO servicoDTO) {
        if (servicoRepository.existsByNome(servicoDTO.getNome())) {
            throw new RuntimeException("Já existe um serviço com este nome: " + servicoDTO.getNome());
        }

        Servico servico = new Servico();
        servico.setNome(servicoDTO.getNome());
        servico.setDescricao(servicoDTO.getDescricao());
        servico.setPreco(servicoDTO.getPreco()); // ✅ BigDecimal para BigDecimal
        servico.setDuracaoMinutos(servicoDTO.getDuracaoMinutos());
        servico.setAtivo(true);

        return servicoRepository.save(servico);
    }

    // ... outros métodos

    // ✅ Usar BigDecimal nos parâmetros
    public List<Servico> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return servicoRepository.findByPrecoBetween(precoMin, precoMax);
    }

    public List<Servico> listarTodos() {
        return null;
    }

    public Servico desativar(Long id) {
        return null;
    }

    public List<Servico> listarAtivos() {
        return null;
    }

    public Servico buscarPorId(Long id) {
        return null;
    }

    public Servico buscarPorNome(String nome) {
        return null;
    }

    public Servico atualizar(Long id, @Valid ServicoDTO servicoDTO) {
        return null;
    }

    public void deletar(Long id) {

    }

    public Servico ativar(Long id) {
        return null;
    }

    public List<Servico> buscarPorDuracao(Integer duracaoMin, Integer duracaoMax) {
        return null;
    }
}