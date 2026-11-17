package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.shared.NivelRisco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PontoRepository extends JpaRepository<PontoMonitorado, Long> {

    Page<PontoMonitorado> findByNivelRiscoAtualIn(Collection<NivelRisco> niveis, Pageable pageable);
}
