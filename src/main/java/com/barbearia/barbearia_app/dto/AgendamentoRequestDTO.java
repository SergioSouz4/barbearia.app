package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoRequestDTO {

    @NotNull(message = "Cliente é obrigatório")
    @Positive(message = "ID do cliente deve ser positivo")
    private Long clienteId;

    @NotNull(message = "Barbeiro é obrigatório")
    @Positive(message = "ID do barbeiro deve ser positivo")
    private Long barbeiroId;

    @NotNull(message = "Serviço é obrigatório")
    @Positive(message = "ID do serviço deve ser positivo")
    private Long servicoId;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser no futuro")
    private LocalDateTime dataHora;

    private String observacoes;
}