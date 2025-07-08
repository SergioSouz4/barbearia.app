package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class AdminLoginRequestDTO {
    @NotBlank(message="Informe seu Email")
    @Email(message="Email deve ser válido")
    private String email;

    @NotBlank(message="Senha é Obrigatória")
    private String senha;
}
