package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.*;
import com.barbearia.barbearia_app.model.*;
import com.barbearia.barbearia_app.model.enums.StatusAgendamento;
import com.barbearia.barbearia_app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;
    private final HorarioFuncionamentoRepository horarioFuncionamentoRepository;
    private final ComissaoService comissaoService;

    @Transactional
    public AgendamentoResponseDTO criar(AgendamentoRequestDTO request) {
        // Validar se cliente existe
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Validar se barbeiro existe
        Barbeiro barbeiro = barbeiroRepository.findById(request.getBarbeiroId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Validar se serviço existe
        Servico servico = servicoRepository.findById(request.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Validar se barbeiro oferece o serviço
        if (!barbeiro.getServicos().contains(servico)) {
            throw new RuntimeException("Barbeiro não oferece este serviço");
        }

        // Validar disponibilidade
        validarDisponibilidade(request.getBarbeiroId(), request.getDataHora(), servico.getDuracaoMinutos());

        // Criar agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setDataHora(request.getDataHora());
        agendamento.setValorTotal(servico.getPreco());
        agendamento.setObservacoes(request.getObservacoes());
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return converterParaResponseDTO(agendamentoSalvo);
    }

    public List<AgendamentoResponseDTO> listarTodos() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteIdOrderByDataHoraDesc(clienteId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorBarbeiro(Long barbeiroId) {
        return agendamentoRepository.findByBarbeiroIdOrderByDataHora(barbeiroId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorBarbeiroEData(Long barbeiroId, LocalDate data) {
        LocalDateTime inicioData = data.atStartOfDay();
        return agendamentoRepository.findByBarbeiroIdAndData(barbeiroId, inicioData)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarProximosCliente(Long clienteId) {
        return agendamentoRepository.findProximosAgendamentosCliente(clienteId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public AgendamentoResponseDTO buscarPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        return converterParaResponseDTO(agendamento);
    }

    @Transactional
    public AgendamentoResponseDTO confirmar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Apenas agendamentos com status 'AGENDADO' podem ser confirmados");
        }

        agendamento.confirmar();
        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);
        return converterParaResponseDTO(agendamentoAtualizado);
    }

    @Transactional
    public AgendamentoResponseDTO realizar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.CONFIRMADO &&
                agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Agendamento não pode ser realizado com status atual");
        }

        agendamento.realizar();
        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);

        // Criar comissão
        comissaoService.criarComissao(agendamentoAtualizado);

        return converterParaResponseDTO(agendamentoAtualizado);
    }

    @Transactional
    public AgendamentoResponseDTO cancelar(Long id, String motivo) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (agendamento.getStatus() == StatusAgendamento.REALIZADO) {
            throw new RuntimeException("Agendamento já realizado não pode ser cancelado");
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RuntimeException("Agendamento já está cancelado");
        }

        agendamento.cancelar(motivo);
        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);
        return converterParaResponseDTO(agendamentoAtualizado);
    }

    @Transactional
    public AgendamentoResponseDTO marcarNaoCompareceu(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.CONFIRMADO &&
                agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Agendamento não pode ser marcado como 'não compareceu'");
        }

        agendamento.naoCompareceu();
        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);
        return converterParaResponseDTO(agendamentoAtualizado);
    }

    public List<HorarioDisponivelDTO> buscarHorariosDisponiveis(DisponibilidadeRequestDTO request) {
        List<HorarioDisponivelDTO> horariosDisponiveis = new ArrayList<>();

        // Buscar horário de funcionamento do barbeiro no dia solicitado
        var horarioFuncionamento = horarioFuncionamentoRepository
                .findByBarbeiroIdAndDiaSemanaAndAtivoTrue(request.getBarbeiroId(), request.getData().getDayOfWeek());

        if (horarioFuncionamento.isEmpty()) {
            return horariosDisponiveis; // Barbeiro não trabalha neste dia
        }

        HorarioFuncionamento horario = horarioFuncionamento.get();

        // Buscar duração do serviço
        int duracaoServico = 30; // Padrão
        if (request.getServicoId() != null) {
            Servico servico = servicoRepository.findById(request.getServicoId()).orElse(null);
            if (servico != null) {
                duracaoServico = servico.getDuracaoMinutos();
            }
        }

        // Gerar horários de 30 em 30 minutos
        LocalTime horaAtual = horario.getHoraInicio();
        LocalTime horaFim = horario.getHoraFim().minusMinutes(duracaoServico);

        while (!horaAtual.isAfter(horaFim)) {
            LocalDateTime dataHoraCompleta = LocalDateTime.of(request.getData(), horaAtual);

            // Verificar se não é no passado
            if (dataHoraCompleta.isAfter(LocalDateTime.now())) {
                boolean disponivel = !agendamentoRepository.existsConflito(
                        request.getBarbeiroId(), dataHoraCompleta);

                HorarioDisponivelDTO horarioDisponivel = new HorarioDisponivelDTO(
                        dataHoraCompleta, disponivel);

                if (!disponivel) {
                    horarioDisponivel.setMotivo("Horário já agendado");
                }

                horariosDisponiveis.add(horarioDisponivel);
            }

            horaAtual = horaAtual.plusMinutes(30);
        }

        return horariosDisponiveis;
    }

    private void validarDisponibilidade(Long barbeiroId, LocalDateTime dataHora, int duracaoMinutos) {
        // Verificar se não é no passado
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar para data/hora no passado");
        }

        // Verificar se barbeiro trabalha neste dia
        boolean trabalhaEm = horarioFuncionamentoRepository
                .barbeiroTrabalhaEm(barbeiroId, dataHora.getDayOfWeek());

        if (!trabalhaEm) {
            throw new RuntimeException("Barbeiro não trabalha neste dia da semana");
        }

        // Verificar horário de funcionamento
        var horarioFuncionamento = horarioFuncionamentoRepository
                .findByBarbeiroIdAndDiaSemanaAndAtivoTrue(barbeiroId, dataHora.getDayOfWeek());

        if (horarioFuncionamento.isPresent()) {
            HorarioFuncionamento horario = horarioFuncionamento.get();
            LocalTime horaAgendamento = dataHora.toLocalTime();
            LocalTime horaFimServico = horaAgendamento.plusMinutes(duracaoMinutos);

            if (horaAgendamento.isBefore(horario.getHoraInicio()) ||
                    horaFimServico.isAfter(horario.getHoraFim())) {
                throw new RuntimeException("Horário fora do funcionamento do barbeiro");
            }
        }

        // Verificar conflito de horário
        if (agendamentoRepository.existsConflito(barbeiroId, dataHora)) {
            throw new RuntimeException("Barbeiro já possui agendamento neste horário");
        }
    }

    // Métodos de conversão
    private AgendamentoResponseDTO converterParaResponseDTO(Agendamento agendamento) {
        return new AgendamentoResponseDTO(
                agendamento.getId(),
                agendamento.getCliente().getNome(),
                agendamento.getCliente().getTelefone(),
                agendamento.getBarbeiro().getNome(),
                agendamento.getServico().getNome(),
                agendamento.getServico().getDuracaoMinutos(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                agendamento.getValorTotal(),
                agendamento.getObservacoes(),
                agendamento.getDataCriacao(),
                agendamento.getDataAtualizacao()
        );
    }
}