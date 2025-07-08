package com.barbearia.barbearia_app.config;

import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Criar usuário administrador se não existir
        if (!usuarioRepository.existsByEmail("sergio.s.souza4142@gmail.com")) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("sergio.s.souza4142@gmail.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setAtivo(true);
            admin.setRoles(List.of("ROLE_ADMIN"));

            usuarioRepository.save(admin);
            System.out.println("Usuário administrador criado: sergio.s.souza4142@gmail.com / admin123");
        }
    }
}