package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // ✅ APENAS MÉTODOS BÁSICOS - SEM REFERÊNCIA A 'ativo'
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByTelefone(String telefone);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findByDataCadastroBetween(LocalDateTime inicio, LocalDateTime fim);

    // ✅ REMOVEMOS TODOS OS MÉTODOS QUE USAM 'ativo'
}