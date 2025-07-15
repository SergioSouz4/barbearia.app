package com.barbearia.barbearia_app.model.enums;

public enum TipoUsuario {
    CLIENTE("Cliente"),
    BARBEIRO("Barbeiro"),
    ADMINISTRADOR("Administrador");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}