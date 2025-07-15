package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_funcionamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioFuncionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek diaSemana;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFim;

    @Column(nullable = false)
    private boolean ativo = true;

    // Construtor para facilitar criação
    public HorarioFuncionamento(Barbeiro barbeiro, DayOfWeek diaSemana,
                                LocalTime horaInicio, LocalTime horaFim) {
        this.barbeiro = barbeiro;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.ativo = true;
    }

    // Método para verificar se um horário está dentro do funcionamento
    public boolean contemHorario(LocalTime horario) {
        return !horario.isBefore(horaInicio) && !horario.isAfter(horaFim);
    }
}