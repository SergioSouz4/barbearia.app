package com.barbearia.barbearia_app.dto;

import com.barbearia.barbearia_app.model.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponseDTO {

    private Long id;
    private String clienteNome;
    private String clienteTelefone;
    private String barbeiroNome;
    private String servicoNome;
    private Integer servicoDuracao;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private BigDecimal valorTotal;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Construtor para facilitar a conversão
    public AgendamentoResponseDTO(Long id, String clienteNome, String clienteTelefone,
                                  String barbeiroNome, String servicoNome, Integer servicoDuracao,
                                  LocalDateTime dataHora, StatusAgendamento status,
                                  BigDecimal valorTotal, String observacoes,
                                  LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.clienteNome = clienteNome;
        this.clienteTelefone = clienteTelefone;
        this.barbeiroNome = barbeiroNome;
        this.servicoNome = servicoNome;
        this.servicoDuracao = servicoDuracao;
        this.dataHora = dataHora;
        this.status = status;
        this.valorTotal = valorTotal;
        this.observacoes = observacoes;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }
}