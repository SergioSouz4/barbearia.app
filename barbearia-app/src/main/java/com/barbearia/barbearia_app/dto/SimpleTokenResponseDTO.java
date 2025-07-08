package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleTokenResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private String nome;
    private String role;

    public SimpleTokenResponseDTO(String token, String nome, String role) {
        this.token = token;
        this.nome = nome;
        this.role = role;
    }
}