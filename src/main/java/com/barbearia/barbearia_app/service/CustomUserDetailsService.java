package com.barbearia.barbearia_app.service;

import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = Optional.ofNullable(usuarioRepository.findByEmail(email));
        Usuario usuario = usuarioOpt.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        // ✅ CORREÇÃO AQUI:
        List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name())
        );

        return new User(usuario.getEmail(), usuario.getSenha(), usuario.isAtivo(), true, true, true, authorities);
    }
}