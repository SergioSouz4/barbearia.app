package com.barbearia.barbearia_app.model.enums;

public enum StatusAgendamento {
    AGENDADO("Agendado"),
    CONFIRMADO("Confirmado"),
    REALIZADO("Realizado"),
    CANCELADO("Cancelado"),
    NAO_COMPARECEU("Não Compareceu");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}