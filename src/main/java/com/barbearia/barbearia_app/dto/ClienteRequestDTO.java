package com.barbearia.barbearia_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "(d{2})sd{4,5}-d{4}",
            message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
    private String telefone;

    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
}