package com.barbearia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    private String telefone;
    
    private LocalDate dataNascimento;
    
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}