package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Agendamento;
import com.barbearia.barbearia_app.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByClienteId(Long clienteId);
    List<Agendamento> findByBarbeiroId(Long barbeiroId);

    @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId AND a.dataHoraInicio BETWEEN :inicio AND :fim")
    List<Agendamento> findByBarbeiroAndPeriodo(Long barbeiroId, LocalDateTime inicio, LocalDateTime fim);

    List<Agendamento> findByStatus(StatusAgendamento status);

    @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio >= :hoje ORDER BY a.dataHoraInicio")
    List<Agendamento> findAgendamentosFuturos(LocalDateTime hoje);
}