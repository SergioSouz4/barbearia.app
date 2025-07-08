package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // Use LocalDate para data de nascimento se for só data
import java.time.LocalDateTime; // Use LocalDateTime para data e hora do cadastro

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Mudando para Long para consistência com outros IDs (Barbeiro, Servico)

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String telefone;

    private LocalDate dataNascimento; // Campo para data de nascimento

    @Column(nullable = false) // ✅ Adicionando o campo dataCadastro
    private LocalDateTime dataCadastro; // Data e hora do cadastro

    // Relacionamento com a entidade Usuario (se o cliente tiver um login/usuário)
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}