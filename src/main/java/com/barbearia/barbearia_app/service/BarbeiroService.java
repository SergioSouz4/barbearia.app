package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.*;
import com.barbearia.barbearia_app.model.Barbeiro;
import com.barbearia.barbearia_app.model.Servico;
import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.model.enums.TipoUsuario;
import com.barbearia.barbearia_app.repository.BarbeiroRepository;
import com.barbearia.barbearia_app.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public BarbeiroResponseDTO criar(BarbeiroRequestDTO request) {
        // Verificar se telefone já existe
        if (barbeiroRepository.existsByTelefone(request.getTelefone())) {
            throw new RuntimeException("Telefone já cadastrado");
        }

        // Criar usuário se email e senha foram fornecidos
        Usuario usuario = null;
        if (request.getEmail() != null && request.getSenha() != null) {
            if (usuarioService.existePorEmail(request.getEmail())) {
                throw new RuntimeException("Email já cadastrado");
            }

            usuario = new Usuario();
            usuario.setEmail(request.getEmail());
            usuario.setSenha(request.getSenha());
            usuario.setTipo(TipoUsuario.BARBEIRO);
            usuario = usuarioService.salvar(usuario);
        }

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(request.getNome());
        barbeiro.setTelefone(request.getTelefone());
        barbeiro.setEspecialidade(request.getEspecialidade());
        barbeiro.setUsuario(usuario);

        if (request.getPercentualComissao() != null) {
            barbeiro.setPercentualComissao(request.getPercentualComissao());
        }

        // Associar serviços
        if (request.getServicosIds() != null && !request.getServicosIds().isEmpty()) {
            List<Servico> servicos = servicoRepository.findAllById(request.getServicosIds());
            barbeiro.setServicos(servicos);
        }

        Barbeiro barbeiroSalvo = barbeiroRepository.save(barbeiro);
        return converterParaResponseDTO(barbeiroSalvo);
    }

    public List<BarbeiroResponseDTO> listarTodos() {
        return barbeiroRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BarbeiroResumoDTO> listarResumo() {
        return barbeiroRepository.findByAtivoTrueOrderByNome()
                .stream()
                .map(this::converterParaResumoDTO)
                .collect(Collectors.toList());
    }

    public BarbeiroResponseDTO buscarPorId(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
        return converterParaResponseDTO(barbeiro);
    }

    public Barbeiro buscarPorUsuarioId(Long usuarioId) {
        return barbeiroRepository.findByUsuarioId(usuarioId)
                .orElse(null);
    }

    public List<BarbeiroResponseDTO> buscarPorNome(String nome) {
        return barbeiroRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BarbeiroResumoDTO> buscarPorServico(Long servicoId) {
        return barbeiroRepository.findByServicosIdAndAtivoTrue(servicoId)
                .stream()
                .map(this::converterParaResumoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BarbeiroResponseDTO atualizar(Long id, BarbeiroRequestDTO request) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Verificar se telefone já existe (exceto o próprio barbeiro)
        if (!barbeiro.getTelefone().equals(request.getTelefone()) &&
                barbeiroRepository.existsByTelefone(request.getTelefone())) {
            throw new RuntimeException("Telefone já cadastrado");
        }

        barbeiro.setNome(request.getNome());
        barbeiro.setTelefone(request.getTelefone());
        barbeiro.setEspecialidade(request.getEspecialidade());

        if (request.getPercentualComissao() != null) {
            barbeiro.setPercentualComissao(request.getPercentualComissao());
        }

        // Atualizar serviços
        if (request.getServicosIds() != null) {
            List<Servico> servicos = servicoRepository.findAllById(request.getServicosIds());
            barbeiro.setServicos(servicos);
        }

        Barbeiro barbeiroAtualizado = barbeiroRepository.save(barbeiro);
        return converterParaResponseDTO(barbeiroAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Soft delete - apenas desativa o barbeiro
        barbeiro.setAtivo(false);
        barbeiroRepository.save(barbeiro);

        // Desativar usuário também
        if (barbeiro.getUsuario() != null) {
            usuarioService.desativarUsuario(barbeiro.getUsuario().getId());
        }
    }

    @Transactional
    public void ativar(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        barbeiro.setAtivo(true);
        barbeiroRepository.save(barbeiro);

        // Ativar usuário também
        if (barbeiro.getUsuario() != null) {
            usuarioService.ativarUsuario(barbeiro.getUsuario().getId());
        }
    }

    public Long contarTotal() {
        return barbeiroRepository.count();
    }

    public Long contarAtivos() {
        return barbeiroRepository.countByAtivoTrue();
    }

    // Métodos de conversão
    private BarbeiroResponseDTO converterParaResponseDTO(Barbeiro barbeiro) {
        BarbeiroResponseDTO dto = new BarbeiroResponseDTO();
        dto.setId(barbeiro.getId());
        dto.setNome(barbeiro.getNome());
        dto.setTelefone(barbeiro.getTelefone());
        dto.setEspecialidade(barbeiro.getEspecialidade());
        dto.setPercentualComissao(barbeiro.getPercentualComissao());
        dto.setDataCadastro(barbeiro.getDataCadastro());
        dto.setAtivo(barbeiro.isAtivo());

        if (barbeiro.getUsuario() != null) {
            dto.setUsuarioId(barbeiro.getUsuario().getId());
        }

        // Converter serviços
        if (barbeiro.getServicos() != null) {
            List<ServicoResponseDTO> servicosDTO = barbeiro.getServicos().stream()
                    .map(this::converterServicoParaResponseDTO)
                    .collect(Collectors.toList());
            dto.setServicos(servicosDTO);
        }

        // Calcular estatísticas (se necessário)
        if (barbeiro.getAgendamentos() != null) {
            dto.setTotalAgendamentos(barbeiro.getAgendamentos().size());

            barbeiro.getAgendamentos().stream()
                    .map(agendamento -> agendamento.getDataHora())
                    .max(LocalDateTime::compareTo)
                    .ifPresent(dto::setUltimoAtendimento);
        }

        if (barbeiro.getComissoes() != null) {
            dto.setTotalComissoes(barbeiro.getComissoes().stream()
                    .map(comissao -> comissao.getValorComissao())
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            dto.setComissoesNaoPagas(barbeiro.getComissoes().stream()
                    .filter(comissao -> !comissao.isPago())
                    .map(comissao -> comissao.getValorComissao())
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return dto;
    }

    private BarbeiroResumoDTO converterParaResumoDTO(Barbeiro barbeiro) {
        BarbeiroResumoDTO dto = new BarbeiroResumoDTO();
        dto.setId(barbeiro.getId());
        dto.setNome(barbeiro.getNome());
        dto.setEspecialidade(barbeiro.getEspecialidade());
        dto.setAtivo(barbeiro.isAtivo());

        // Converter serviços para resumo
        if (barbeiro.getServicos() != null) {
            List<ServicoResumoDTO> servicosResumo = barbeiro.getServicos().stream()
                    .map(this::converterServicoParaResumoDTO)
                    .collect(Collectors.toList());
            dto.setServicos(servicosResumo);
        }

        // Dias de funcionamento (implementar quando tivermos HorarioFuncionamento)
        // TODO: Implementar após criar HorarioFuncionamentoService

        return dto;
    }

    private ServicoResponseDTO converterServicoParaResponseDTO(Servico servico) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracaoMinutos(),
                servico.isAtivo(),
                servico.getDataCriacao(),
                servico.getDataAtualizacao()
        );
    }

    private ServicoResumoDTO converterServicoParaResumoDTO(Servico servico) {
        return new ServicoResumoDTO(
                servico.getId(),
                servico.getNome(),
                servico.getPreco(),
                servico.getDuracaoMinutos()
        );
    }

    public Barbeiro buscarPorTelefone(String telefone) {
        return null;
    }

    public List<Barbeiro> buscarPorEspecialidade(String especialidade) {
        return null;
    }
}