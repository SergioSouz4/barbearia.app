package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    boolean existsByTelefone(String telefone);

    List<Barbeiro> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    List<Barbeiro> findByAtivoTrueOrderByNome();

    Optional<Barbeiro> findByUsuarioId(Long usuarioId);

    @Query("SELECT b FROM Barbeiro b JOIN b.servicos s WHERE s.id = :servicoId AND b.ativo = true")
    List<Barbeiro> findByServicosIdAndAtivoTrue(@Param("servicoId") Long servicoId);

    Long countByAtivoTrue();
}