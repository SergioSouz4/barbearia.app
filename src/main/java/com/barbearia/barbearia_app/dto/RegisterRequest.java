package com.barbearia.barbearia_app.dto;

import com.barbearia.barbearia_app.model.enums.TipoUsuario;

// DTO para registrar um novo usuário genérico (Cliente ou Barbeiro)
public record RegisterRequest(
        String email,
        String senha,
        TipoUsuario tipoUsuario, // Usar CLIENTE, BARBEIRO ou ADMINISTRADOR
        String telefone, // Para Cliente
        String especialidades // Para Barbeiro
) {
}