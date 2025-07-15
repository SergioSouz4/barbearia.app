package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios e estatísticas do sistema")
@SecurityRequirement(name = "bearerAuth")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/geral")
    @Operation(summary = "Relatório geral", description = "Relatório geral da barbearia com estatísticas principais")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> gerarRelatorioGeral() {
        Map<String, Object> relatorio = relatorioService.gerarRelatorioGeral();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    @Operation(summary = "Relatório por barbeiro", description = "Relatório detalhado de um barbeiro específico")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> gerarRelatorioBarbeiro(
            @PathVariable Long barbeiroId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().withDayOfMonth(1)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        Map<String, Object> relatorio = relatorioService.gerarRelatorioBarbeiro(barbeiroId, inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/financeiro")
    @Operation(summary = "Relatório financeiro", description = "Relatório financeiro detalhado por período")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> gerarRelatorioFinanceiro(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().withDayOfMonth(1)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        Map<String, Object> relatorio = relatorioService.gerarRelatorioFinanceiro(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Estatísticas rápidas", description = "Estatísticas rápidas para dashboard")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> gerarEstatisticasRapidas() {
        Map<String, Object> estatisticas = relatorioService.gerarEstatisticasRapidas();
        return ResponseEntity.ok(estatisticas);
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dados do dashboard", description = "Dados completos para o dashboard administrativo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> gerarDadosDashboard() {
        Map<String, Object> dashboard = relatorioService.gerarRelatorioGeral();
        Map<String, Object> estatisticas = relatorioService.gerarEstatisticasRapidas();

        // Combinar dados
        dashboard.putAll(estatisticas);

        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/barbeiro/{barbeiroId}/dashboard")
    @Operation(summary = "Dashboard do barbeiro", description = "Dashboard específico para barbeiros")
    @PreAuthorize("hasAnyRole('BARBEIRO', 'ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> gerarDashboardBarbeiro(@PathVariable Long barbeiroId) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate hoje = LocalDate.now();

        Map<String, Object> dashboard = relatorioService.gerarRelatorioBarbeiro(barbeiroId, inicioMes, hoje);
        Map<String, Object> estatisticas = relatorioService.gerarEstatisticasRapidas();

        // Adicionar estatísticas gerais
        dashboard.put("estatisticasGerais", estatisticas);

        return ResponseEntity.ok(dashboard);
    }
}