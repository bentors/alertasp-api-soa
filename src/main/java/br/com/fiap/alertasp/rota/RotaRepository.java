package br.com.fiap.alertasp.rota;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RotaRepository extends JpaRepository<Rota, Long> {

    List<Rota> findByUsuarioIdOrderByNome(Long usuarioId);

    Optional<Rota> findByIdAndUsuarioId(Long id, Long usuarioId);
}
