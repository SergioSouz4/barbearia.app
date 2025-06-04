package com.barbearia.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "$$\d{2}$$\s\d{4,5}-\d{4}", message = "Formato de telefone inválido. Use (99) 99999-9999")
    private String telefone;

    private LocalDate dataNascimento;

    private Long usuarioId;
}