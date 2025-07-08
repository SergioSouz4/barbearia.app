package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.BarbeiroDTO;
import com.barbearia.barbearia_app.model.Barbeiro;
import com.barbearia.barbearia_app.repository.BarbeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    public Barbeiro criar(BarbeiroDTO barbeiroDTO) {
        // Verificar se já existe um barbeiro com o mesmo telefone
        if (barbeiroRepository.existsByTelefone(barbeiroDTO.getTelefone())) {
            throw new RuntimeException("Já existe um barbeiro com este telefone: " + barbeiroDTO.getTelefone());
        }

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(barbeiroDTO.getNome());
        barbeiro.setTelefone(barbeiroDTO.getTelefone());
        barbeiro.setEspecialidade(barbeiroDTO.getEspecialidade());
        barbeiro.setAtivo(true); // Por padrão, novo barbeiro fica ativo

        return barbeiroRepository.save(barbeiro);
    }

    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll();
    }

    public List<Barbeiro> listarAtivos() {
        return barbeiroRepository.findByTelefone();
    }

    public Barbeiro buscarPorId(Long id) {
        return barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado com ID: " + id));
    }

    public Barbeiro buscarPorTelefone(String telefone) {
        return barbeiroRepository.findByTelefone(telefone)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado com telefone: " + telefone));
    }

    public List<Barbeiro> buscarPorEspecialidade(String especialidade) {
        return barbeiroRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }

    public List<Barbeiro> buscarPorNome(String nome) {
        return barbeiroRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Barbeiro atualizar(Long id, BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = buscarPorId(id);

        // Verificar se o novo telefone não conflita com outro barbeiro
        if (!barbeiro.getTelefone().equals(barbeiroDTO.getTelefone()) &&
                barbeiroRepository.existsByTelefone(barbeiroDTO.getTelefone())) {
            throw new RuntimeException("Já existe um barbeiro com este telefone: " + barbeiroDTO.getTelefone());
        }

        barbeiro.setNome(barbeiroDTO.getNome());
        barbeiro.setTelefone(barbeiroDTO.getTelefone());
        barbeiro.setEspecialidade(barbeiroDTO.getEspecialidade());

        return barbeiroRepository.save(barbeiro);
    }

    public void deletar(Long id) {
        Barbeiro barbeiro = buscarPorId(id);

        // Verificar se o barbeiro tem agendamentos futuros
        if (barbeiroRepository.temAgendamentosFuturos(id)) {
            // Em vez de deletar, desativar o barbeiro
            barbeiro.setAtivo(false);
            barbeiroRepository.save(barbeiro);
            throw new RuntimeException("Barbeiro não pode ser deletado pois possui agendamentos futuros. Foi desativado.");
        }

        barbeiroRepository.delete(barbeiro);
    }

    public Barbeiro ativar(Long id) {
        Barbeiro barbeiro = buscarPorId(id);
        barbeiro.setAtivo(true);
        return barbeiroRepository.save(barbeiro);
    }

    public Barbeiro desativar(Long id) {
        Barbeiro barbeiro = buscarPorId(id);
        barbeiro.setAtivo(false);
        return barbeiroRepository.save(barbeiro);
    }

    public List<Barbeiro> buscarDisponiveis(LocalDateTime dataHora) {
        return barbeiroRepository.findBarbeirosDisponiveis(dataHora);
    }

    public List<Barbeiro> buscarPorDisponibilidadeNaData(LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(23, 59, 59);
        return barbeiroRepository.findBarbeirosComDisponibilidade(inicioDia, fimDia);
    }

    public boolean verificarDisponibilidade(Long barbeiroId, LocalDateTime dataHora, Integer duracaoMinutos) {
        LocalDateTime dataHoraFim = dataHora.plusMinutes(duracaoMinutos);
        return barbeiroRepository.verificarDisponibilidade(barbeiroId, dataHora, dataHoraFim);
    }

    public Long contarAgendamentos(Long barbeiroId) {
        return barbeiroRepository.contarAgendamentosPorBarbeiro(barbeiroId);
    }

    public Long contarAgendamentosNoMes(Long barbeiroId, int ano, int mes) {
        return barbeiroRepository.contarAgendamentosNoMes(barbeiroId, ano, mes);
    }

    public List<Barbeiro> buscarMaisPopulares() {
        return barbeiroRepository.findBarbeirosMaisPopulares();
    }
}