package com.barbearia.service;

import com.barbearia.dto.AgendamentoDTO;
import com.barbearia.exception.BusinessException;
import com.barbearia.exception.ResourceNotFoundException;
import com.barbearia.model.*;
import com.barbearia.repository.AgendamentoRepository;
import com.barbearia.repository.BarbeiroRepository;
import com.barbearia.repository.ClienteRepository;
import com.barbearia.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarPorBarbeiro(Long barbeiroId) {
        return agendamentoRepository.findByBarbeiroId(barbeiroId);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosFuturos() {
        return agendamentoRepository.findAgendamentosFuturos(LocalDateTime.now());
    }

    @Transactional
    public Agendamento criar(AgendamentoDTO agendamentoDTO) {
        // Buscar entidades relacionadas
        Cliente cliente = clienteRepository.findById(agendamentoDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Barbeiro barbeiro = barbeiroRepository.findById(agendamentoDTO.getBarbeiroId())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado"));

        Servico servico = servicoRepository.findById(agendamentoDTO.getServicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        // Verificar se o barbeiro oferece o serviço
        if (!barbeiro.getServicos().contains(servico)) {
            throw new BusinessException("Este barbeiro não oferece o serviço selecionado");
        }

        // Verificar se a data é futura
        if (agendamentoDTO.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar para uma data passada");
        }

        // Verificar disponibilidade do barbeiro
        LocalDateTime dataHoraFim = agendamentoDTO.getDataHoraInicio().plus(servico.getDuracao());
        List<Agendamento> agendamentosExistentes = agendamentoRepository.findByBarbeiroAndPeriodo(
                barbeiro.getId(),
                agendamentoDTO.getDataHoraInicio(),
                dataHoraFim
        );

        if (!agendamentosExistentes.isEmpty()) {
            throw new BusinessException("Horário não disponível para este barbeiro");
        }

        // Criar o agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setDataHoraInicio(agendamentoDTO.getDataHoraInicio());
        agendamento.setDataHoraFim(dataHoraFim);
        agendamento.setObservacoes(agendamentoDTO.getObservacoes());

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento atualizar(Long id, AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = buscarPorId(id);

        // Verificar se o agendamento já foi concluído ou cancelado
        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO ||
                agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível alterar um agendamento concluído ou cancelado");
        }

        // Atualizar apenas os campos permitidos
        agendamento.setObservacoes(agendamentoDTO.getObservacoes());

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento alterarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void cancelar(Long id) {
        alterarStatus(id, StatusAgendamento.CANCELADO);
    }
}
