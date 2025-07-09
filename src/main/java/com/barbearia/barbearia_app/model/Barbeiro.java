package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "barbeiros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String telefone;

    private String especialidade;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentualComissao = new BigDecimal("50.00"); // 50% padrão

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    private boolean ativo = true;

    // Relacionamento com Usuario
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Relacionamento com Agendamentos
    @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;

    // Relacionamento com Serviços que o barbeiro oferece
    @ManyToMany
    @JoinTable(
            name = "barbeiro_servicos",
            joinColumns = @JoinColumn(name = "barbeiro_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos;

    // Relacionamento com Horários de Funcionamento
    @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.ALL)
    private List<HorarioFuncionamento> horariosFuncionamento;

    // Relacionamento com Comissões
    @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.ALL)
    private List<Comissao> comissoes;

    // Construtor para facilitar criação
    public Barbeiro(String nome, String telefone, String especialidade) {
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
        this.percentualComissao = new BigDecimal("50.00");
    }
}