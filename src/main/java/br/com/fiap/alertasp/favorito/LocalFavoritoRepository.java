package br.com.fiap.alertasp.favorito;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocalFavoritoRepository extends JpaRepository<LocalFavorito, Long> {

    List<LocalFavorito> findByUsuarioIdOrderByNome(Long usuarioId);

    Optional<LocalFavorito> findByIdAndUsuarioId(Long id, Long usuarioId);
}
