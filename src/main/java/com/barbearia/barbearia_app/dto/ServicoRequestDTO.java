package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoRequestDTO {

    @NotBlank(message = "Nome do serviço é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @DecimalMax(value = "9999.99", message = "Preço deve ser menor que R$ 9.999,99")
    @Digits(integer = 4, fraction = 2, message = "Preço deve ter no máximo 4 dígitos inteiros e 2 decimais")
    private BigDecimal preco;

    @NotNull(message = "Duração em minutos é obrigatória")
    @Min(value = 5, message = "Duração mínima é de 5 minutos")
    @Max(value = 480, message = "Duração máxima é de 8 horas (480 minutos)")
    private Integer duracaoMinutos;
}