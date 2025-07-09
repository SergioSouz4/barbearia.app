package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResumoDTO {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private int totalAgendamentos;
    private LocalDateTime ultimoAgendamento;
    private boolean ativo;

    // Construtor sem estatísticas
    public ClienteResumoDTO(Long id, String nome, String telefone, String email, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.ativo = ativo;
        this.totalAgendamentos = 0;
    }
}