package com.barbearia.repository;

import com.barbearia.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByTelefone(String telefone);
    Optional<Cliente> findByUsuarioId(Long usuarioId);
}