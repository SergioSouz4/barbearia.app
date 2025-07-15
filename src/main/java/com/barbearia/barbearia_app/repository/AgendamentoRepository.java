package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Agendamento;
import com.barbearia.barbearia_app.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Buscar agendamentos por cliente
    List<Agendamento> findByClienteIdOrderByDataHoraDesc(Long clienteId);

    // Buscar agendamentos por barbeiro
    List<Agendamento> findByBarbeiroIdOrderByDataHora(Long barbeiroId);

    // Buscar agendamentos por barbeiro e data
    @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
            "AND DATE(a.dataHora) = DATE(:data) ORDER BY a.dataHora")
    List<Agendamento> findByBarbeiroIdAndData(@Param("barbeiroId") Long barbeiroId,
                                              @Param("data") LocalDateTime data);

    // Buscar agendamentos por status
    List<Agendamento> findByStatusOrderByDataHora(StatusAgendamento status);

    // Verificar conflito de horário
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
            "AND a.dataHora = :dataHora AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU')")
    boolean existsConflito(@Param("barbeiroId") Long barbeiroId,
                           @Param("dataHora") LocalDateTime dataHora);

    // Buscar agendamentos em um período
    @Query("SELECT a FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim " +
            "ORDER BY a.dataHora")
    List<Agendamento> findByPeriodo(@Param("inicio") LocalDateTime inicio,
                                    @Param("fim") LocalDateTime fim);

    // Buscar agendamentos por barbeiro em um período
    @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
            "AND a.dataHora BETWEEN :inicio AND :fim ORDER BY a.dataHora")
    List<Agendamento> findByBarbeiroIdAndPeriodo(@Param("barbeiroId") Long barbeiroId,
                                                 @Param("inicio") LocalDateTime inicio,
                                                 @Param("fim") LocalDateTime fim);

    // Contar agendamentos por barbeiro e status
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
            "AND a.status = :status")
    Long countByBarbeiroIdAndStatus(@Param("barbeiroId") Long barbeiroId,
                                    @Param("status") StatusAgendamento status);

    // Buscar próximos agendamentos do cliente
    @Query("SELECT a FROM Agendamento a WHERE a.cliente.id = :clienteId " +
            "AND a.dataHora > CURRENT_TIMESTAMP AND a.status NOT IN ('CANCELADO', 'REALIZADO') " +
            "ORDER BY a.dataHora")
    List<Agendamento> findProximosAgendamentosCliente(@Param("clienteId") Long clienteId);
}