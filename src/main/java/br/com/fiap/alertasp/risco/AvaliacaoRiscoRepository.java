package br.com.fiap.alertasp.risco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRiscoRepository extends JpaRepository<AvaliacaoRisco, Long> {

    Page<AvaliacaoRisco> findByPontoIdOrderByCriadaEmDescIdDesc(Long pontoId, Pageable pageable);
}
