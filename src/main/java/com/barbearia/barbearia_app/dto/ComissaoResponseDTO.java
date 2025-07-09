package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComissaoResponseDTO {

    private Long id;
    private Long barbeiroId;
    private String barbeiroNome;
    private Long agendamentoId;
    private String clienteNome;
    private String servicoNome;
    private BigDecimal percentualComissao;
    private BigDecimal valorServico;
    private BigDecimal valorComissao;
    private LocalDate dataReferencia;
    private LocalDateTime dataCriacao;
    private boolean pago;
    private LocalDateTime dataPagamento;
}