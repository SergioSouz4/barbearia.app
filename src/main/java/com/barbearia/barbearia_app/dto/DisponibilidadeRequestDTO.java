package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeRequestDTO {

    @NotNull(message = "Barbeiro é obrigatório")
    @Positive(message = "ID do barbeiro deve ser positivo")
    private Long barbeiroId;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @Positive(message = "ID do serviço deve ser positivo")
    private Long servicoId; // Para calcular duração
}