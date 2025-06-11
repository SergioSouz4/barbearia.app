package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    private String observacoes;
}
