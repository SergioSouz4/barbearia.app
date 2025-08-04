package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginNomeTelefoneDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
}
