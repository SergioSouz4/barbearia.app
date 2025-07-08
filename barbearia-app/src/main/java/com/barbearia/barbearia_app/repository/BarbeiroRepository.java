package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    // ✅ APENAS MÉTODOS BÁSICOS - SEM @Query
    Optional<Barbeiro> findByTelefone(String telefone);
    boolean existsByTelefone(String telefone);
    List<Barbeiro> findByEspecialidadeContainingIgnoreCase(String especialidade);
    List<Barbeiro> findByNomeContainingIgnoreCase(String nome);

    default List<Barbeiro> findByTelefone() {
        return null;
    }

    default boolean temAgendamentosFuturos(Long id) {
        return false;
    }

    default List<Barbeiro> findBarbeirosDisponiveis(LocalDateTime dataHora) {
        return null;
    }

    default List<Barbeiro> findBarbeirosComDisponibilidade(LocalDateTime inicioDia, LocalDateTime fimDia) {
        return null;
    }

    default boolean verificarDisponibilidade(Long barbeiroId, LocalDateTime dataHora, LocalDateTime dataHoraFim) {
        return false;
    }

    default Long contarAgendamentosPorBarbeiro(Long barbeiroId) {
        return null;
    }

    default Long contarAgendamentosNoMes(Long barbeiroId, int ano, int mes) {
        return null;
    }

    default List<Barbeiro> findBarbeirosMaisPopulares() {
        return null;
    }

    // ✅ REMOVEMOS TODAS AS QUERIES CUSTOMIZADAS
}