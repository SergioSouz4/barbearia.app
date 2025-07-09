package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioFuncionamentoResponseDTO {

    private Long id;
    private Long barbeiroId;
    private String barbeiroNome;
    private DayOfWeek diaSemana;
    private String diaSemanaDescricao;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private boolean ativo;

    // Método para converter DayOfWeek para português
    public String getDiaSemanaDescricao() {
        return switch (diaSemana) {
            case MONDAY -> "Segunda-feira";
            case TUESDAY -> "Terça-feira";
            case WEDNESDAY -> "Quarta-feira";
            case THURSDAY -> "Quinta-feira";
            case FRIDAY -> "Sexta-feira";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}