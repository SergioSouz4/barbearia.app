package com.barbearia.barbearia_app.dto;

import com.barbearia.barbearia_app.model.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String email;
    private TipoUsuario tipoUsuario;
    private String nome; // Nome do cliente/barbeiro
    private Long perfilId; // ID do cliente ou barbeiro

    public LoginResponseDTO(String token, Long usuarioId, String email,
                            TipoUsuario tipoUsuario, String nome, Long perfilId) {
        this.token = token;
        this.tipo = "Bearer";
        this.usuarioId = usuarioId;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.perfilId = perfilId;
    }
}