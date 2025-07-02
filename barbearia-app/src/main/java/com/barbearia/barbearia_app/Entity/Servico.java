package com.barbearia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Duration;

@Data
@Entity
@Table(name = "servicos")
public class Servico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    @Column(nullable = false)
    private Duration duracao; // Duração em minutos
    
    private String imagem;
}