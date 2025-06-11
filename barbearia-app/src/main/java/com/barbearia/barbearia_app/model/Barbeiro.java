package com.barbearia.barbearia_app.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String especialidade;

    @Column(length = 500)
    private String biografia;

    private String fotoPerfil;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "barbeiro_servicos",
            joinColumns = @JoinColumn(name = "barbeiro_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos = new ArrayList<>();
}