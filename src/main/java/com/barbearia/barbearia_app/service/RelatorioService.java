package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.model.enums.StatusAgendamento;
import com.barbearia.barbearia_app.repository.AgendamentoRepository;
import com.barbearia.barbearia_app.repository.BarbeiroRepository;
import com.barbearia.barbearia_app.repository.ClienteRepository;
import com.barbearia.barbearia_app.repository.ComissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final AgendamentoRepository agendamentoRepository;
    private final ComissaoRepository comissaoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;

    public Map<String, Object> gerarRelatorioGeral() {
        Map<String, Object> relatorio = new HashMap<>();

        // Totais gerais
        relatorio.put("totalClientes", clienteRepository.count());
        relatorio.put("totalClientesAtivos", clienteRepository.countByAtivoTrue());
        relatorio.put("totalBarbeiros", barbeiroRepository.count());
        relatorio.put("totalBarbeirosAtivos", barbeiroRepository.countByAtivoTrue());

        // Agendamentos
        relatorio.put("totalAgendamentos", agendamentoRepository.count());
        relatorio.put("agendamentosRealizados",
                agendamentoRepository.findByStatusOrderByDataHora(StatusAgendamento.REALIZADO).size());
        relatorio.put("agendamentosCancelados",
                agendamentoRepository.findByStatusOrderByDataHora(StatusAgendamento.CANCELADO).size());
        relatorio.put("agendamentosNaoCompareceu",
                agendamentoRepository.findByStatusOrderByDataHora(StatusAgendamento.NAO_COMPARECEU).size());
        relatorio.put("agendamentosAgendados",
                agendamentoRepository.findByStatusOrderByDataHora(StatusAgendamento.AGENDADO).size());
        relatorio.put("agendamentosConfirmados",
                agendamentoRepository.findByStatusOrderByDataHora(StatusAgendamento.CONFIRMADO).size());

        // Financeiro do mês atual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now();
        relatorio.put("totalComissoesMesAtual", comissaoRepository.calcularTotalComissoesPeriodo(inicioMes, fimMes));

        // Agendamentos do mês atual
        LocalDateTime inicioMesDateTime = inicioMes.atStartOfDay();
        LocalDateTime fimMesDateTime = fimMes.atTime(23, 59, 59);

        var agendamentosMes = agendamentoRepository.findByPeriodo(inicioMesDateTime, fimMesDateTime);
        BigDecimal faturamentoMes = agendamentosMes.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.REALIZADO)
                .map(a -> a.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        relatorio.put("faturamentoMesAtual", faturamentoMes);
        relatorio.put("agendamentosMesAtual", agendamentosMes.size());

        return relatorio;
    }

    public Map<String, Object> gerarRelatorioBarbeiro(Long barbeiroId, LocalDate inicio, LocalDate fim) {
        Map<String, Object> relatorio = new HashMap<>();

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        // Agendamentos do barbeiro no período
        var agendamentos = agendamentoRepository.findByBarbeiroIdAndPeriodo(barbeiroId, inicioDateTime, fimDateTime);

        relatorio.put("totalAgendamentos", agendamentos.size());
        relatorio.put("agendamentosRealizados",
                agendamentos.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.REALIZADO ? 1 : 0).sum());
        relatorio.put("agendamentosCancelados",
                agendamentos.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.CANCELADO ? 1 : 0).sum());
        relatorio.put("agendamentosNaoCompareceu",
                agendamentos.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.NAO_COMPARECEU ? 1 : 0).sum());

        // Faturamento gerado pelo barbeiro
        BigDecimal faturamentoGerado = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.REALIZADO)
                .map(a -> a.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        relatorio.put("faturamentoGerado", faturamentoGerado);

        // Comissões do barbeiro no período
        var comissoes = comissaoRepository.findByBarbeiroIdAndPeriodo(barbeiroId, inicio, fim);
        BigDecimal totalComissoes = comissoes.stream()
                .map(c -> c.getValorComissao())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal comissoesNaoPagas = comissoes.stream()
                .filter(c -> !c.isPago())
                .map(c -> c.getValorComissao())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        relatorio.put("totalComissoes", totalComissoes);
        relatorio.put("comissoesNaoPagas", comissoesNaoPagas);
        relatorio.put("comissoesPagas", totalComissoes.subtract(comissoesNaoPagas));

        // Estatísticas adicionais
        relatorio.put("totalAtendimentos",
                agendamentos.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.REALIZADO ? 1 : 0).sum());

        // Taxa de comparecimento
        long totalAgendados = agendamentos.stream()
                .mapToLong(a -> (a.getStatus() == StatusAgendamento.REALIZADO ||
                        a.getStatus() == StatusAgendamento.NAO_COMPARECEU) ? 1 : 0).sum();

        long totalCompareceu = agendamentos.stream()
                .mapToLong(a -> a.getStatus() == StatusAgendamento.REALIZADO ? 1 : 0).sum();

        BigDecimal taxaComparecimento = totalAgendados > 0 ?
                new BigDecimal(totalCompareceu).divide(new BigDecimal(totalAgendados), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")) : BigDecimal.ZERO;

        relatorio.put("taxaComparecimento", taxaComparecimento);

        return relatorio;
    }

    public Map<String, Object> gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        Map<String, Object> relatorio = new HashMap<>();

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        // Agendamentos realizados no período
        var agendamentosRealizados = agendamentoRepository.findByPeriodo(inicioDateTime, fimDateTime)
                .stream()
                .filter(a -> a.getStatus() == StatusAgendamento.REALIZADO)
                .toList();

        BigDecimal faturamentoTotal = agendamentosRealizados.stream()
                .map(a -> a.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalComissoes = comissaoRepository.calcularTotalComissoesPeriodo(inicio, fim);
        BigDecimal lucroLiquido = faturamentoTotal.subtract(totalComissoes);

        relatorio.put("faturamentoTotal", faturamentoTotal);
        relatorio.put("totalComissoes", totalComissoes);
        relatorio.put("lucroLiquido", lucroLiquido);
        relatorio.put("totalAtendimentos", agendamentosRealizados.size());

        // Ticket médio corrigido
        relatorio.put("ticketMedio", agendamentosRealizados.isEmpty() ? BigDecimal.ZERO :
                faturamentoTotal.divide(new BigDecimal(agendamentosRealizados.size()), 2, RoundingMode.HALF_UP));

        // Estatísticas adicionais
        var todosAgendamentos = agendamentoRepository.findByPeriodo(inicioDateTime, fimDateTime);

        relatorio.put("totalAgendamentos", todosAgendamentos.size());
        relatorio.put("agendamentosCancelados",
                todosAgendamentos.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.CANCELADO ? 1 : 0).sum());
        relatorio.put("agendamentosNaoCompareceu",
                todosAgendamentos.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.NAO_COMPARECEU ? 1 : 0).sum());

        // Taxa de conversão (realizados vs agendados)
        BigDecimal taxaConversao = todosAgendamentos.isEmpty() ? BigDecimal.ZERO :
                new BigDecimal(agendamentosRealizados.size())
                        .divide(new BigDecimal(todosAgendamentos.size()), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));

        relatorio.put("taxaConversao", taxaConversao);

        // Comissões pagas vs não pagas
        var todasComissoes = comissaoRepository.findByPeriodo(inicio, fim);
        BigDecimal comissoesNaoPagas = todasComissoes.stream()
                .filter(c -> !c.isPago())
                .map(c -> c.getValorComissao())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        relatorio.put("comissoesPagas", totalComissoes.subtract(comissoesNaoPagas));
        relatorio.put("comissoesNaoPagas", comissoesNaoPagas);

        return relatorio;
    }

    // Métodos auxiliares para relatórios específicos
    public Map<String, Object> gerarEstatisticasRapidas() {
        Map<String, Object> stats = new HashMap<>();

        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioHoje = hoje.atStartOfDay();
        LocalDateTime fimHoje = hoje.atTime(23, 59, 59);

        // Agendamentos de hoje
        var agendamentosHoje = agendamentoRepository.findByPeriodo(inicioHoje, fimHoje);
        stats.put("agendamentosHoje", agendamentosHoje.size());
        stats.put("agendamentosRealizadosHoje",
                agendamentosHoje.stream().mapToLong(a -> a.getStatus() == StatusAgendamento.REALIZADO ? 1 : 0).sum());

        // Próximos agendamentos (próximos 7 dias)
        LocalDateTime proximaSemana = LocalDateTime.now().plusDays(7);
        var proximosAgendamentos = agendamentoRepository.findByPeriodo(LocalDateTime.now(), proximaSemana);
        stats.put("proximosAgendamentos", proximosAgendamentos.size());

        return stats;
    }
}