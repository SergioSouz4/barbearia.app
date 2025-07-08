package com.barbearia.barbearia_app.dto;

import com.barbearia.barbearia_app.model.Servico;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AgendamentoDTO {
    private Long id;

    @NotNull(message = "O cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O barbeiro é obrigatório")
    private Long barbeiroId;

    @NotNull(message = "O serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "A data e hora são obrigatórias")
    private LocalDateTime dataHoraInicio;

    private String observacoes;

    public Long getClienteId() {
        return 0L;
    }

    public Long getBarbeiroId() {
        return null;
    }

    public Long getServicoId() {
        return null;
    }

    public LocalDateTime getDataHoraInicio() {
        return null;
    }

    public Servico getObservacoes() {
        return null;
    }
}

