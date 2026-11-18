package br.com.fiap.alertasp.rota;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RotaRepository extends JpaRepository<Rota, Long> {

    @EntityGraph(attributePaths = "pontos")
    List<Rota> findByUsuarioIdOrderByNome(Long usuarioId);

    @EntityGraph(attributePaths = "pontos")
    Optional<Rota> findByIdAndUsuarioId(Long id, Long usuarioId);
}
