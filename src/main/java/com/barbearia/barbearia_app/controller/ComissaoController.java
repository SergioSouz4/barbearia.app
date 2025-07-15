package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.ComissaoResponseDTO;
import com.barbearia.barbearia_app.service.ComissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comissoes")
@RequiredArgsConstructor
@Tag(name = "Comissões", description = "Gerenciamento de comissões dos barbeiros")
@SecurityRequirement(name = "bearerAuth")
public class ComissaoController {

    private final ComissaoService comissaoService;

    @GetMapping("/barbeiro/{barbeiroId}")
    @Operation(summary = "Buscar comissões por barbeiro", description = "Lista todas as comissões de um barbeiro")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<List<ComissaoResponseDTO>> buscarPorBarbeiro(@PathVariable Long barbeiroId) {
        List<ComissaoResponseDTO> comissoes = comissaoService.buscarPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(comissoes);
    }

    @GetMapping("/barbeiro/{barbeiroId}/nao-pagas")
    @Operation(summary = "Buscar comissões não pagas", description = "Lista comissões não pagas de um barbeiro")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<List<ComissaoResponseDTO>> buscarNaoPagasPorBarbeiro(@PathVariable Long barbeiroId) {
        List<ComissaoResponseDTO> comissoes = comissaoService.buscarNaoPagasPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(comissoes);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar comissões por período", description = "Lista comissões em um período específico")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ComissaoResponseDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<ComissaoResponseDTO> comissoes = comissaoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(comissoes);
    }

    @GetMapping("/barbeiro/{barbeiroId}/periodo")
    @Operation(summary = "Buscar comissões por barbeiro e período", description = "Lista comissões de um barbeiro em um período")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<List<ComissaoResponseDTO>> buscarPorBarbeiroEPeriodo(
            @PathVariable Long barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<ComissaoResponseDTO> comissoes = comissaoService.buscarPorBarbeiroEPeriodo(barbeiroId, inicio, fim);
        return ResponseEntity.ok(comissoes);
    }

    @PutMapping("/{id}/pagar")
    @Operation(summary = "Marcar comissão como paga", description = "Marca uma comissão específica como paga")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ComissaoResponseDTO> marcarComoPago(@PathVariable Long id) {
        ComissaoResponseDTO comissao = comissaoService.marcarComoPago(id);
        return ResponseEntity.ok(comissao);
    }

    @PutMapping("/pagar-lote")
    @Operation(summary = "Pagar comissões em lote", description = "Marca múltiplas comissões como pagas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ComissaoResponseDTO>> pagarEmLote(@RequestBody List<Long> ids) {
        List<ComissaoResponseDTO> comissoes = comissaoService.marcarComoPagemLote(ids);
        return ResponseEntity.ok(comissoes);
    }

    @GetMapping("/barbeiro/{barbeiroId}/totais")
    @Operation(summary = "Totais de comissões do barbeiro", description = "Retorna totais de comissões de um barbeiro")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<Map<String, BigDecimal>> calcularTotaisBarbeiro(@PathVariable Long barbeiroId) {
        BigDecimal total = comissaoService.calcularTotalComissoesBarbeiro(barbeiroId);
        BigDecimal naoPagas = comissaoService.calcularTotalComissoesNaoPagasBarbeiro(barbeiroId);

        Map<String, BigDecimal> totais = Map.of(
                "totalComissoes", total,
                "comissoesNaoPagas", naoPagas,
                "comissoesPagas", total.subtract(naoPagas)
        );

        return ResponseEntity.ok(totais);
    }

    @GetMapping("/periodo/total")
    @Operation(summary = "Total de comissões por período", description = "Calcula total de comissões em um período")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, BigDecimal>> calcularTotalPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        BigDecimal total = comissaoService.calcularTotalComissoesPeriodo(inicio, fim);

        Map<String, BigDecimal> resultado = Map.of(
                "totalComissoes", total,
                "periodo", total
        );

        return ResponseEntity.ok(resultado);
    }
}