package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);

    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    List<Cliente> findByAtivoTrueOrderByNome();

    Optional<Cliente> findByUsuarioId(Long usuarioId);
    Optional<Cliente> findByEmail(String email);

    Long countByAtivoTrue();
}