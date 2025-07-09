package com.barbearia.barbearia_app.controller;

import com.barbearia.barbearia_app.dto.*;
import com.barbearia.barbearia_app.service.BarbeiroService;
import com.barbearia.barbearia_app.service.ServicoService;
import com.barbearia.barbearia_app.service.HorarioFuncionamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Público", description = "Endpoints públicos (sem autenticação)")
public class PublicoController {

    private final ServicoService servicoService;
    private final BarbeiroService barbeiroService;
    private final HorarioFuncionamentoService horarioFuncionamentoService;

    @GetMapping("/servicos")
    @Operation(summary = "Listar serviços", description = "Lista todos os serviços disponíveis (público)")
    public ResponseEntity<List<ServicoResumoDTO>> listarServicos() {
        List<ServicoResumoDTO> servicos = servicoService.listarResumo();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/barbeiros")
    @Operation(summary = "Listar barbeiros", description = "Lista todos os barbeiros ativos (público)")
    public ResponseEntity<List<BarbeiroResumoDTO>> listarBarbeiros() {
        List<BarbeiroResumoDTO> barbeiros = barbeiroService.listarResumo();
        return ResponseEntity.ok(barbeiros);
    }

    @GetMapping("/barbeiros/{id}")
    @Operation(summary = "Detalhes do barbeiro", description = "Detalhes públicos de um barbeiro específico")
    public ResponseEntity<Map<String, Object>> detalhesBarbeiro(@PathVariable Long id) {
        BarbeiroResponseDTO barbeiro = barbeiroService.buscarPorId(id);
        List<HorarioFuncionamentoResponseDTO> horarios = horarioFuncionamentoService.buscarPorBarbeiro(id);

        Map<String, Object> detalhes = Map.of(
                "barbeiro", barbeiro,
                "horariosFuncionamento", horarios
        );

        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/barbeiros/servico/{servicoId}")
    @Operation(summary = "Barbeiros por serviço", description = "Lista barbeiros que oferecem um serviço específico")
    public ResponseEntity<List<BarbeiroResumoDTO>> buscarBarbeirosPorServico(@PathVariable Long servicoId) {
        List<BarbeiroResumoDTO> barbeiros = barbeiroService.buscarPorServico(servicoId);
        return ResponseEntity.ok(barbeiros);
    }

    @GetMapping("/horarios-funcionamento/dia/{diaSemana}")
    @Operation(summary = "Barbeiros por dia", description = "Lista barbeiros que trabalham em um dia específico")
    public ResponseEntity<List<Long>> buscarBarbeirosPorDia(@PathVariable DayOfWeek diaSemana) {
        List<Long> barbeirosIds = horarioFuncionamentoService.buscarBarbeirosQueTrabalhamEm(diaSemana);
        return ResponseEntity.ok(barbeirosIds);
    }

    @GetMapping("/informacoes")
    @Operation(summary = "Informações da barbearia", description = "Informações gerais da barbearia")
    public ResponseEntity<Map<String, Object>> informacoesBarbearia() {
        Map<String, Object> info = Map.of(
                "nome", "Barbearia Premium",
                "descricao", "A melhor barbearia da cidade",
                "telefone", "(11) 99999-9999",
                "endereco", "Rua das Flores, 123 - Centro",
                "horarioGeral", "Segunda a Sexta: 8h às 18h | Sábado: 8h às 16h",
                "totalBarbeiros", barbeiroService.contarAtivos(),
                "totalServicos", servicoService.contarAtivos()
        );

        return ResponseEntity.ok(info);
    }
}