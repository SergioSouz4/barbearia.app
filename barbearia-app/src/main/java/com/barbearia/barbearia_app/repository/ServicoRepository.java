package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    Optional<Servico> findByNome(String nome);
    boolean existsByNome(String nome);
    List<Servico> findByAtivoTrue();

    // ✅ Usar BigDecimal nos parâmetros
    @Query("SELECT s FROM Servico s WHERE s.preco BETWEEN :precoMin AND :precoMax AND s.ativo = true")
    List<Servico> findByPrecoBetween(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax);

    @Query("SELECT s FROM Servico s WHERE s.duracaoMinutos BETWEEN :duracaoMin AND :duracaoMax AND s.ativo = true")
    List<Servico> findByDuracaoMinutosBetween(@Param("duracaoMin") Integer duracaoMin, @Param("duracaoMax") Integer duracaoMax);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.servico.id = :servicoId")
    boolean temAgendamentosAssociados(@Param("servicoId") Long servicoId);
}