package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.HorarioFuncionamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioFuncionamentoRepository extends JpaRepository<HorarioFuncionamento, Long> {

    // Buscar horários por barbeiro
    List<HorarioFuncionamento> findByBarbeiroIdAndAtivoTrueOrderByDiaSemana(Long barbeiroId);

    // Buscar horário específico do barbeiro em um dia
    Optional<HorarioFuncionamento> findByBarbeiroIdAndDiaSemanaAndAtivoTrue(
            Long barbeiroId, DayOfWeek diaSemana);

    // Verificar se barbeiro trabalha em um dia específico
    @Query("SELECT COUNT(h) > 0 FROM HorarioFuncionamento h WHERE h.barbeiro.id = :barbeiroId " +
            "AND h.diaSemana = :diaSemana AND h.ativo = true")
    boolean barbeiroTrabalhaEm(@Param("barbeiroId") Long barbeiroId,
                               @Param("diaSemana") DayOfWeek diaSemana);

    // Buscar todos os barbeiros que trabalham em um dia específico
    @Query("SELECT DISTINCT h.barbeiro.id FROM HorarioFuncionamento h " +
            "WHERE h.diaSemana = :diaSemana AND h.ativo = true")
    List<Long> findBarbeirosQueTrabalhamEm(@Param("diaSemana") DayOfWeek diaSemana);
}