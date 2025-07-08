package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TokenRequestDTO {
    @NotBlank(message="Qual o seu nome?")
    @Size(min=2,max=100,message="Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message="Token obrigatório")
    private String captchaToken;

}
