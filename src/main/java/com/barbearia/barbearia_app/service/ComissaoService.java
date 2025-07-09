package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.ComissaoResponseDTO;
import com.barbearia.barbearia_app.model.Agendamento;
import com.barbearia.barbearia_app.model.Comissao;
import com.barbearia.barbearia_app.repository.ComissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComissaoService {

    private final ComissaoRepository comissaoRepository;

    @Transactional
    public ComissaoResponseDTO criarComissao(Agendamento agendamento) {
        // Verificar se já existe comissão para este agendamento
        if (agendamento.getComissao() != null) {
            throw new RuntimeException("Comissão já foi criada para este agendamento");
        }

        Comissao comissao = new Comissao(agendamento.getBarbeiro(), agendamento);
        Comissao comissaoSalva = comissaoRepository.save(comissao);

        return converterParaResponseDTO(comissaoSalva);
    }

    public List<ComissaoResponseDTO> buscarPorBarbeiro(Long barbeiroId) {
        return comissaoRepository.findByBarbeiroIdOrderByDataReferenciaDesc(barbeiroId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ComissaoResponseDTO> buscarNaoPagasPorBarbeiro(Long barbeiroId) {
        return comissaoRepository.findByBarbeiroIdAndPagoFalseOrderByDataReferencia(barbeiroId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ComissaoResponseDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return comissaoRepository.findByPeriodo(inicio, fim)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ComissaoResponseDTO> buscarPorBarbeiroEPeriodo(Long barbeiroId, LocalDate inicio, LocalDate fim) {
        return comissaoRepository.findByBarbeiroIdAndPeriodo(barbeiroId, inicio, fim)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComissaoResponseDTO marcarComoPago(Long id) {
        Comissao comissao = comissaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comissão não encontrada"));

        if (comissao.isPago()) {
            throw new RuntimeException("Comissão já foi paga");
        }

        comissao.marcarComoPago();
        Comissao comissaoAtualizada = comissaoRepository.save(comissao);

        return converterParaResponseDTO(comissaoAtualizada);
    }

    @Transactional
    public List<ComissaoResponseDTO> marcarComoPagemLote(List<Long> ids) {
        List<Comissao> comissoes = comissaoRepository.findAllById(ids);

        for (Comissao comissao : comissoes) {
            if (!comissao.isPago()) {
                comissao.marcarComoPago();
            }
        }

        List<Comissao> comissoesAtualizadas = comissaoRepository.saveAll(comissoes);

        return comissoesAtualizadas.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    // Métodos de cálculo
    public BigDecimal calcularTotalComissoesBarbeiro(Long barbeiroId) {
        return comissaoRepository.calcularTotalComissoesBarbeiro(barbeiroId);
    }

    public BigDecimal calcularTotalComissoesNaoPagasBarbeiro(Long barbeiroId) {
        return comissaoRepository.calcularTotalComissoesNaoPagasBarbeiro(barbeiroId);
    }

    public BigDecimal calcularTotalComissoesPeriodo(LocalDate inicio, LocalDate fim) {
        return comissaoRepository.calcularTotalComissoesPeriodo(inicio, fim);
    }

    // Método de conversão
    private ComissaoResponseDTO converterParaResponseDTO(Comissao comissao) {
        ComissaoResponseDTO dto = new ComissaoResponseDTO();
        dto.setId(comissao.getId());
        dto.setBarbeiroId(comissao.getBarbeiro().getId());
        dto.setBarbeiroNome(comissao.getBarbeiro().getNome());
        dto.setAgendamentoId(comissao.getAgendamento().getId());
        dto.setClienteNome(comissao.getAgendamento().getCliente().getNome());
        dto.setServicoNome(comissao.getAgendamento().getServico().getNome());
        dto.setPercentualComissao(comissao.getPercentualComissao());
        dto.setValorServico(comissao.getValorServico());
        dto.setValorComissao(comissao.getValorComissao());
        dto.setDataReferencia(comissao.getDataReferencia());
        dto.setDataCriacao(comissao.getDataCriacao());
        dto.setPago(comissao.isPago());
        dto.setDataPagamento(comissao.getDataPagamento());
        return dto;
    }
}