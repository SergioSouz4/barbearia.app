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
    boolean existsByNomeAndTelefone (String nome, String telefone);


    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Cliente> findByAtivoTrueOrderByNome();

    Optional<Cliente> findByUsuarioId(Long usuarioId);  // corrigido o método que estava com erro

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByNomeAndTelefone(String nome, String telefone);  // busca cliente por nome + telefone

    Long countByAtivoTrue();
}