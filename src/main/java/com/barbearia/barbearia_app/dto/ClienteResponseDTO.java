package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;
    private boolean ativo;
    private Long usuarioId;
    private int totalAgendamentos;
    private LocalDateTime ultimoAgendamento;

    // Construtor básico (sem estatísticas)
    public ClienteResponseDTO(Long id, String nome, String email, String telefone,
                              LocalDate dataNascimento, LocalDateTime dataCadastro,
                              boolean ativo, Long usuarioId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
        this.usuarioId = usuarioId;
        this.totalAgendamentos = 0;
    }

    // Construtor completo (com estatísticas)
    public ClienteResponseDTO(Long id, String nome, String email, String telefone,
                              LocalDate dataNascimento, LocalDateTime dataCadastro,
                              boolean ativo, Long usuarioId, int totalAgendamentos,
                              LocalDateTime ultimoAgendamento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
        this.usuarioId = usuarioId;
        this.totalAgendamentos = totalAgendamentos;
        this.ultimoAgendamento = ultimoAgendamento;
    }
}