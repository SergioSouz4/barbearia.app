package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.dto.UsuarioDTO;
import com.barbearia.barbearia_app.exception.ResourceNotFoundException;
import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.repository.UsuarioRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    public Usuario criar(UsuarioDTO usuarioDTO) throws ResourceAlreadyExistsException {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setRoles((List<String>) Set.of("ROLE_USER"));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioDTO usuarioDTO) throws ResourceAlreadyExistsException {
        Usuario usuario = buscarPorId(id);

        usuario.setNome((String) usuarioDTO.getNome());

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

    public Usuario buscarPorEmail(@NotBlank(message = "Informe seu Email") @Email(message = "Email deve ser válido") String email) {
        return null;
    }

    public void deletar(Long id) {

    }
}
