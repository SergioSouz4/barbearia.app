package com.barbearia.barbearia_app.repository;

import com.barbearia.barbearia_app.model.Usuario;
import com.barbearia.barbearia_app.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByTipo(TipoUsuario tipo);

    List<Usuario> findByAtivoTrue();

    Optional<Usuario> findByEmailAndAtivoTrue(String email);
}