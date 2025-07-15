package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracaoMinutos;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Campos calculados
    private String duracaoFormatada;
    private String precoFormatado;

    // Construtor principal
    public ServicoResponseDTO(Long id, String nome, String descricao, BigDecimal preco,
                              Integer duracaoMinutos, boolean ativo, LocalDateTime dataCriacao,
                              LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;

        // Calcular campos formatados
        this.duracaoFormatada = formatarDuracao(duracaoMinutos);
        this.precoFormatado = String.format("R$ %.2f", preco);
    }

    private String formatarDuracao(Integer minutos) {
        if (minutos == null) return "0min";

        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;

        if (horas > 0) {
            if (minutosRestantes > 0) {
                return String.format("%dh %dmin", horas, minutosRestantes);
            } else {
                return String.format("%dh", horas);
            }
        } else {
            return String.format("%dmin", minutos);
        }
    }
}