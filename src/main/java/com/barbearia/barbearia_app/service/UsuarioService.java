package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.model.Cliente;
import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.repository.ClienteRepository;
import com.barbearia.barbearia_app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;  // injetar clienteRepository para buscar cliente
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Para manter compatibilidade com Spring Security (exemplo por email)
        return usuarioRepository.findByEmailAndAtivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public Usuario autenticarPorNomeETelefoneSemSenha(String nome, String telefone) {
        Cliente cliente = clienteRepository.findByNomeAndTelefone(nome, telefone)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com nome e telefone fornecidos"));

        Usuario usuario = cliente.getUsuario();

        if (usuario == null || !usuario.isAtivo()) {
            throw new RuntimeException("Usuário inativo ou não encontrado");
        }

        // Não valida senha, libera o acesso diretamente
        return usuario;
    }

    public Usuario salvar(Usuario usuario) {
        // Criptografar senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void desativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void ativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }
}