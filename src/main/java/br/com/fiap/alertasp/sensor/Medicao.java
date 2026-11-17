package br.com.fiap.alertasp.sensor;

import br.com.fiap.alertasp.shared.NivelRisco;
import br.com.fiap.alertasp.shared.RegraRisco;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Leitura enviada por um sensor: distância até a superfície e o nível de água derivado dela. */
@Entity
@Table(name = "medicao")
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Column(name = "distancia_cm")
    private double distanciaCm;

    @Column(name = "nivel_agua_cm")
    private double nivelAguaCm;

    @Column(name = "registrada_em")
    private LocalDateTime registradaEm = LocalDateTime.now();

    protected Medicao() {
    }

    public Medicao(Sensor sensor, double distanciaCm) {
        this.sensor = sensor;
        this.distanciaCm = distanciaCm;
        this.nivelAguaCm = RegraRisco.nivelAgua(sensor.getAlturaInstalacaoCm(), distanciaCm);
    }

    public NivelRisco nivelRisco() {
        return RegraRisco.nivelPorMedicao(nivelAguaCm, sensor.getLimiteAlertaCm());
    }

    public Long getId() { return id; }
    public Sensor getSensor() { return sensor; }
    public double getDistanciaCm() { return distanciaCm; }
    public double getNivelAguaCm() { return nivelAguaCm; }
    public LocalDateTime getRegistradaEm() { return registradaEm; }
}
