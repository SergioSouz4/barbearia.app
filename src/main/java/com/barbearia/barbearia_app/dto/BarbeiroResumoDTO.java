package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarbeiroResumoDTO {

    private Long id;
    private String nome;
    private String especialidade;
    private boolean ativo;

    // Serviços que o barbeiro oferece (apenas nome e preço)
    private List<ServicoResumoDTO> servicos;

    // Horários de funcionamento resumidos
    private List<String> diasFuncionamento; // Ex: ["Segunda", "Terça", "Quarta"]

    // Construtor básico
    public BarbeiroResumoDTO(Long id, String nome, String especialidade, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.ativo = ativo;
    }
}