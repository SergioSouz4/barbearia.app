package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

@Entity
@Table(name = "servicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco; // ✅ Usar BigDecimal

    @Column(nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false)
    private boolean ativo = true;

    public TemporalAmount getDuracao() {
        return null;
    }

    public void setDataAtualizacao(LocalDateTime now) {

    }

    public LocalDateTime getDataCriacao() {
        return null;
    }

    public LocalDateTime getDataAtualizacao() {
        return null;
    }
}