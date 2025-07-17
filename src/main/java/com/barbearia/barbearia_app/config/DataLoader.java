package com.barbearia.barbearia_app.config;

import com.barbearia.barbearia_app.model.*;
import com.barbearia.barbearia_app.model.enums.TipoUsuario;
import com.barbearia.barbearia_app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final HorarioFuncionamentoRepository horarioFuncionamentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            criarDadosIniciais();
        }
    }

    private void criarDadosIniciais() {
        System.out.println("🚀 Criando dados iniciais...");

        // 1. Criar usuário administrador
        Usuario adminUser = new Usuario();
        adminUser.setEmail("admin@barbearia.com");
        adminUser.setSenha(passwordEncoder.encode("admin123"));
        adminUser.setTipo(TipoUsuario.ADMINISTRADOR);
        usuarioRepository.save(adminUser);

        // 2. Criar serviços
        List<Servico> servicos = List.of(
                criarServico("Corte Masculino", "Corte tradicional masculino", new BigDecimal("25.00"), 30),
                criarServico("Barba", "Aparar e modelar barba", new BigDecimal("15.00"), 20),
                criarServico("Corte + Barba", "Pacote completo", new BigDecimal("35.00"), 45),
                criarServico("Corte Infantil", "Corte para crianças até 12 anos", new BigDecimal("20.00"), 25),
                criarServico("Sobrancelha", "Design de sobrancelha masculina", new BigDecimal("10.00"), 15)
        );
        servicoRepository.saveAll(servicos);

        // 3. Criar barbeiros (um por vez para evitar problemas de transação)
        Barbeiro barbeiro1 = criarBarbeiro("Carlos Silva", "(11) 99999-1111", "Cortes clássicos",
                new BigDecimal("30.00"), "carlos@barbearia.com");

        // Buscar serviços salvos do banco
        List<Servico> servicosSalvos = servicoRepository.findAll();
        barbeiro1.setServicos(servicosSalvos.subList(0, 3)); // Corte, Barba, Corte+Barba
        barbeiroRepository.save(barbeiro1);

        Barbeiro barbeiro2 = criarBarbeiro("Roberto Santos", "(11) 99999-2222", "Barbas e bigodes",
                new BigDecimal("25.00"), "roberto@barbearia.com");
        barbeiro2.setServicos(servicosSalvos.subList(1, 5)); // Barba, Corte+Barba, Infantil, Sobrancelha
        barbeiroRepository.save(barbeiro2);

        // 4. Criar horários de funcionamento
        criarHorariosFuncionamento(barbeiro1);
        criarHorariosFuncionamento(barbeiro2);

        System.out.println("✅ Dados iniciais criados com sucesso!");
        System.out.println("👨‍💼 Admin: admin@barbearia.com / admin123");
        System.out.println("✂️ Barbeiro 1: carlos@barbearia.com / barbeiro123");
        System.out.println("✂️ Barbeiro 2: roberto@barbearia.com / barbeiro123");
    }

    private Servico criarServico(String nome, String descricao, BigDecimal preco, int duracao) {
        Servico servico = new Servico();
        servico.setNome(nome);
        servico.setDescricao(descricao);
        servico.setPreco(preco);
        servico.setDuracaoMinutos(duracao);
        return servico;
    }

    private Barbeiro criarBarbeiro(String nome, String telefone, String especialidade,
                                   BigDecimal comissao, String email) {
        // Criar usuário para o barbeiro
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("barbeiro123"));
        usuario.setTipo(TipoUsuario.BARBEIRO);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Criar barbeiro com usuário já salvo
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(nome);
        barbeiro.setTelefone(telefone);
        barbeiro.setEspecialidade(especialidade);
        barbeiro.setPercentualComissao(comissao);
        barbeiro.setUsuario(usuarioSalvo); // ← Usar usuário já salvo

        return barbeiro;
    }

    private void criarHorariosFuncionamento(Barbeiro barbeiro) {
        // Segunda a Sexta: 8h às 18h
        for (DayOfWeek dia : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) {
            HorarioFuncionamento horario = new HorarioFuncionamento();
            horario.setBarbeiro(barbeiro);
            horario.setDiaSemana(dia);
            horario.setHoraInicio(LocalTime.of(8, 0));
            horario.setHoraFim(LocalTime.of(18, 0));
            horarioFuncionamentoRepository.save(horario);
        }

        // Sábado: 8h às 16h
        HorarioFuncionamento sabado = new HorarioFuncionamento();
        sabado.setBarbeiro(barbeiro);
        sabado.setDiaSemana(DayOfWeek.SATURDAY);
        sabado.setHoraInicio(LocalTime.of(8, 0));
        sabado.setHoraFim(LocalTime.of(16, 0));
        horarioFuncionamentoRepository.save(sabado);
    }
}