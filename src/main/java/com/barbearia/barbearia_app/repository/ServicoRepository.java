package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    List<Servico> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    List<Servico> findByAtivoTrueOrderByNome();

    Long countByAtivoTrue();
}