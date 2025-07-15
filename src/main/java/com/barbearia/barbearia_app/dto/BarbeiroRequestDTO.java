package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarbeiroRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;


    @Size(max = 200, message = "Especialidade deve ter no máximo 200 caracteres")
    private String especialidade;

    @DecimalMin(value = "0.0", message = "Percentual de comissão deve ser maior ou igual a 0")
    @DecimalMax(value = "100.0", message = "Percentual de comissão deve ser menor ou igual a 100")
    private BigDecimal percentualComissao;

    private List<Long> servicosIds; // IDs dos serviços que o barbeiro oferece

    // Email e senha (apenas para criação)
    @Email(message = "Email deve ter formato válido")
    private String email;

    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;
}