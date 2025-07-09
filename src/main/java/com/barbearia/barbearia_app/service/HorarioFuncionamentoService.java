package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.HorarioFuncionamentoRequestDTO;
import com.barbearia.barbearia_app.dto.HorarioFuncionamentoResponseDTO;
import com.barbearia.barbearia_app.model.Barbeiro;
import com.barbearia.barbearia_app.model.HorarioFuncionamento;
import com.barbearia.barbearia_app.repository.BarbeiroRepository;
import com.barbearia.barbearia_app.repository.HorarioFuncionamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioFuncionamentoService {

    private final HorarioFuncionamentoRepository horarioFuncionamentoRepository;
    private final BarbeiroRepository barbeiroRepository;

    @Transactional
    public HorarioFuncionamentoResponseDTO criar(HorarioFuncionamentoRequestDTO request) {
        // Validar se barbeiro existe
        Barbeiro barbeiro = barbeiroRepository.findById(request.getBarbeiroId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Validar horários
        if (request.getHoraInicio().isAfter(request.getHoraFim()) ||
                request.getHoraInicio().equals(request.getHoraFim())) {
            throw new RuntimeException("Hora de início deve ser anterior à hora de fim");
        }

        // Verificar se já existe horário para este barbeiro neste dia
        var horarioExistente = horarioFuncionamentoRepository
                .findByBarbeiroIdAndDiaSemanaAndAtivoTrue(request.getBarbeiroId(), request.getDiaSemana());

        if (horarioExistente.isPresent()) {
            throw new RuntimeException("Barbeiro já possui horário cadastrado para este dia da semana");
        }

        HorarioFuncionamento horario = new HorarioFuncionamento();
        horario.setBarbeiro(barbeiro);
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFim(request.getHoraFim());

        HorarioFuncionamento horarioSalvo = horarioFuncionamentoRepository.save(horario);
        return converterParaResponseDTO(horarioSalvo);
    }

    public List<HorarioFuncionamentoResponseDTO> buscarPorBarbeiro(Long barbeiroId) {
        return horarioFuncionamentoRepository.findByBarbeiroIdAndAtivoTrueOrderByDiaSemana(barbeiroId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<Long> buscarBarbeirosQueTrabalhamEm(DayOfWeek diaSemana) {
        return horarioFuncionamentoRepository.findBarbeirosQueTrabalhamEm(diaSemana);
    }

    @Transactional
    public HorarioFuncionamentoResponseDTO atualizar(Long id, HorarioFuncionamentoRequestDTO request) {
        HorarioFuncionamento horario = horarioFuncionamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário de funcionamento não encontrado"));

        // Validar horários
        if (request.getHoraInicio().isAfter(request.getHoraFim()) ||
                request.getHoraInicio().equals(request.getHoraFim())) {
            throw new RuntimeException("Hora de início deve ser anterior à hora de fim");
        }

        // Se mudou o dia da semana, verificar se não existe conflito
        if (!horario.getDiaSemana().equals(request.getDiaSemana())) {
            var horarioExistente = horarioFuncionamentoRepository
                    .findByBarbeiroIdAndDiaSemanaAndAtivoTrue(horario.getBarbeiro().getId(), request.getDiaSemana());

            if (horarioExistente.isPresent()) {
                throw new RuntimeException("Barbeiro já possui horário cadastrado para este dia da semana");
            }
        }

        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFim(request.getHoraFim());

        HorarioFuncionamento horarioAtualizado = horarioFuncionamentoRepository.save(horario);
        return converterParaResponseDTO(horarioAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        HorarioFuncionamento horario = horarioFuncionamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário de funcionamento não encontrado"));

        // Soft delete
        horario.setAtivo(false);
        horarioFuncionamentoRepository.save(horario);
    }

    @Transactional
    public void ativar(Long id) {
        HorarioFuncionamento horario = horarioFuncionamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário de funcionamento não encontrado"));

        horario.setAtivo(true);
        horarioFuncionamentoRepository.save(horario);
    }

    @Transactional
    public void criarHorariosPadrao(Long barbeiroId) {
        // Criar horários padrão de segunda a sexta (8h às 18h) e sábado (8h às 16h)
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Segunda a Sexta
        for (DayOfWeek dia : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) {
            var horarioExistente = horarioFuncionamentoRepository
                    .findByBarbeiroIdAndDiaSemanaAndAtivoTrue(barbeiroId, dia);

            if (horarioExistente.isEmpty()) {
                HorarioFuncionamento horario = new HorarioFuncionamento();
                horario.setBarbeiro(barbeiro);
                horario.setDiaSemana(dia);
                horario.setHoraInicio(java.time.LocalTime.of(8, 0));
                horario.setHoraFim(java.time.LocalTime.of(18, 0));
                horarioFuncionamentoRepository.save(horario);
            }
        }

        // Sábado
        var sabadoExistente = horarioFuncionamentoRepository
                .findByBarbeiroIdAndDiaSemanaAndAtivoTrue(barbeiroId, DayOfWeek.SATURDAY);

        if (sabadoExistente.isEmpty()) {
            HorarioFuncionamento horario = new HorarioFuncionamento();
            horario.setBarbeiro(barbeiro);
            horario.setDiaSemana(DayOfWeek.SATURDAY);
            horario.setHoraInicio(java.time.LocalTime.of(8, 0));
            horario.setHoraFim(java.time.LocalTime.of(16, 0));
            horarioFuncionamentoRepository.save(horario);
        }
    }

    // Método de conversão
    private HorarioFuncionamentoResponseDTO converterParaResponseDTO(HorarioFuncionamento horario) {
        HorarioFuncionamentoResponseDTO dto = new HorarioFuncionamentoResponseDTO();
        dto.setId(horario.getId());
        dto.setBarbeiroId(horario.getBarbeiro().getId());
        dto.setBarbeiroNome(horario.getBarbeiro().getNome());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFim(horario.getHoraFim());
        dto.setAtivo(horario.isAtivo());
        return dto;
    }
}