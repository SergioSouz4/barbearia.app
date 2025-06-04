package com.barbearia.repository;

import com.barbearia.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    Optional<Barbeiro> findByUsuarioId(Long usuarioId);
    List<Barbeiro> findByServicosId(Long servicoId);
}