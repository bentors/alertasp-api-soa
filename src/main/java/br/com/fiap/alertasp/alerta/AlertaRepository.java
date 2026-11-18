package br.com.fiap.alertasp.alerta;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    boolean existsByUsuarioIdAndPontoIdAndLidoFalseAndCriadoEmAfter(Long usuarioId, Long pontoId,
                                                                    LocalDateTime desde);

    @EntityGraph(attributePaths = "ponto")
    List<Alerta> findByUsuarioIdOrderByCriadoEmDescIdDesc(Long usuarioId);

    @EntityGraph(attributePaths = "ponto")
    List<Alerta> findByUsuarioIdAndLidoOrderByCriadoEmDescIdDesc(Long usuarioId, boolean lido);
}
