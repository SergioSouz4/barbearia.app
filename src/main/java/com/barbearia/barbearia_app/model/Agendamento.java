package com.barbearia.barbearia_app.model;

import com.barbearia.barbearia_app.model.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    private String observacoes;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    private LocalDateTime dataAtualizacao;

    private LocalDateTime dataConfirmacao;

    private LocalDateTime dataRealizacao;

    private LocalDateTime dataCancelamento;

    private String motivoCancelamento;

    // Relacionamento com Comissão
    @OneToOne(mappedBy = "agendamento", cascade = CascadeType.ALL)
    private Comissao comissao;

    // Método para confirmar agendamento
    public void confirmar() {
        this.status = StatusAgendamento.CONFIRMADO;
        this.dataConfirmacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Método para realizar agendamento
    public void realizar() {
        this.status = StatusAgendamento.REALIZADO;
        this.dataRealizacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Método para cancelar agendamento
    public void cancelar(String motivo) {
        this.status = StatusAgendamento.CANCELADO;
        this.dataCancelamento = LocalDateTime.now();
        this.motivoCancelamento = motivo;
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Método para marcar como não compareceu
    public void naoCompareceu() {
        this.status = StatusAgendamento.NAO_COMPARECEU;
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}