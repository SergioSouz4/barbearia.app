package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoResumoDTO {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer duracaoMinutos;

    // Campos formatados para exibição
    private String precoFormatado;
    private String duracaoFormatada;

    // Construtor principal
    public ServicoResumoDTO(Long id, String nome, BigDecimal preco, Integer duracaoMinutos) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;

        // Formatar campos
        this.precoFormatado = String.format("R\$ %.2f", preco);
        this.duracaoFormatada = formatarDuracao(duracaoMinutos);
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