package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDisponivelDTO {

    private LocalDateTime dataHora;
    private String horarioFormatado; // Ex: "14:30"
    private boolean disponivel;
    private String motivo; // Se não disponível, o motivo

    public HorarioDisponivelDTO(LocalDateTime dataHora, boolean disponivel) {
        this.dataHora = dataHora;
        this.disponivel = disponivel;
        this.horarioFormatado = dataHora.toLocalTime().toString();
    }

    public HorarioDisponivelDTO(LocalDateTime dataHora, boolean disponivel, String motivo) {
        this(dataHora, disponivel);
        this.motivo = motivo;
    }
}