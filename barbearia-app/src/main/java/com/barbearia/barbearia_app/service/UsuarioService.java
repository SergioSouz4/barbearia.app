package com.barbearia.service;

import com.barbearia.dto.UsuarioDTO;
import com.barbearia.exception.ResourceAlreadyExistsException;
import com.barbearia.exception.ResourceNotFoundException;
import com.barbearia.model.Usuario;
import com.barbearia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public Usuario criar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setRoles(Set.of("ROLE_USER"));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(usuarioDTO.getNome());

        // Atualiza o email apenas se for diferente
        if (!usuario.getEmail().equals(usuarioDTO.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new ResourceAlreadyExistsException("Email já cadastrado: " + usuarioDTO.getEmail());
            }
            usuario.setEmail(usuarioDTO.getEmail());
        }

        // Atualiza a senha apenas se for fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
