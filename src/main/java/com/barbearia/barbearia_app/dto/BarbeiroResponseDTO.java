package com.barbearia.barbearia_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarbeiroResponseDTO {

    private Long id;
    private String nome;
    private String telefone;
    private String especialidade;
    private BigDecimal percentualComissao;
    private LocalDateTime dataCadastro;
    private boolean ativo;
    private Long usuarioId;

    // Serviços que o barbeiro oferece
    private List<ServicoResponseDTO> servicos;

    // Horários de funcionamento
    private List<HorarioFuncionamentoResponseDTO> horariosFuncionamento;

    // Estatísticas
    private int totalAgendamentos;
    private BigDecimal totalComissoes;
    private BigDecimal comissoesNaoPagas;
    private LocalDateTime ultimoAtendimento;

    // Construtor básico (sem relacionamentos e estatísticas)
    public BarbeiroResponseDTO(Long id, String nome, String telefone, String especialidade,
                               BigDecimal percentualComissao, LocalDateTime dataCadastro,
                               boolean ativo, Long usuarioId) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.percentualComissao = percentualComissao;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
        this.usuarioId = usuarioId;
    }
}