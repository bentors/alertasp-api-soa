package br.com.fiap.alertasp.sensor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    @EntityGraph(attributePaths = "sensor")
    Page<Medicao> findBySensorIdOrderByRegistradaEmDescIdDesc(Long sensorId, Pageable pageable);
}
