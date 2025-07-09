package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comissoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal percentualComissao;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorServico;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorComissao;

    @Column(nullable = false)
    private LocalDate dataReferencia;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    private boolean pago = false;

    private LocalDateTime dataPagamento;

    // Construtor para facilitar criação
    public Comissao(Barbeiro barbeiro, Agendamento agendamento) {
        this.barbeiro = barbeiro;
        this.agendamento = agendamento;
        this.percentualComissao = barbeiro.getPercentualComissao();
        this.valorServico = agendamento.getValorTotal();
        this.valorComissao = calcularComissao();
        this.dataReferencia = agendamento.getDataHora().toLocalDate();
        this.dataCriacao = LocalDateTime.now();
        this.pago = false;
    }

    // Método para calcular a comissão
    private BigDecimal calcularComissao() {
        return valorServico.multiply(percentualComissao.divide(new BigDecimal("100")));
    }

    // Método para marcar como pago
    public void marcarComoPago() {
        this.pago = true;
        this.dataPagamento = LocalDateTime.now();
    }
}