package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Comissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Long> {

    // Buscar comissões por barbeiro
    List<Comissao> findByBarbeiroIdOrderByDataReferenciaDesc(Long barbeiroId);

    // Buscar comissões não pagas por barbeiro
    List<Comissao> findByBarbeiroIdAndPagoFalseOrderByDataReferencia(Long barbeiroId);

    // Buscar comissões por período
    @Query("SELECT c FROM Comissao c WHERE c.dataReferencia BETWEEN :inicio AND :fim " +
            "ORDER BY c.dataReferencia DESC")
    List<Comissao> findByPeriodo(@Param("inicio") LocalDate inicio,
                                 @Param("fim") LocalDate fim);

    // Buscar comissões por barbeiro e período
    @Query("SELECT c FROM Comissao c WHERE c.barbeiro.id = :barbeiroId " +
            "AND c.dataReferencia BETWEEN :inicio AND :fim ORDER BY c.dataReferencia DESC")
    List<Comissao> findByBarbeiroIdAndPeriodo(@Param("barbeiroId") Long barbeiroId,
                                              @Param("inicio") LocalDate inicio,
                                              @Param("fim") LocalDate fim);

    // Calcular total de comissões por barbeiro
    @Query("SELECT COALESCE(SUM(c.valorComissao), 0) FROM Comissao c " +
            "WHERE c.barbeiro.id = :barbeiroId")
    BigDecimal calcularTotalComissoesBarbeiro(@Param("barbeiroId") Long barbeiroId);

    // Calcular total de comissões não pagas por barbeiro
    @Query("SELECT COALESCE(SUM(c.valorComissao), 0) FROM Comissao c " +
            "WHERE c.barbeiro.id = :barbeiroId AND c.pago = false")
    BigDecimal calcularTotalComissoesNaoPagasBarbeiro(@Param("barbeiroId") Long barbeiroId);

    // Calcular total de comissões por período
    @Query("SELECT COALESCE(SUM(c.valorComissao), 0) FROM Comissao c " +
            "WHERE c.dataReferencia BETWEEN :inicio AND :fim")
    BigDecimal calcularTotalComissoesPeriodo(@Param("inicio") LocalDate inicio,
                                             @Param("fim") LocalDate fim);
}