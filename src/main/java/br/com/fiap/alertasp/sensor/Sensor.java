package br.com.fiap.alertasp.sensor;

import br.com.fiap.alertasp.ponto.PontoMonitorado;
import jakarta.persistence.*;

/** Sensor de nível de água (ESP32 + ultrassônico) instalado acima de um ponto monitorado. */
@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id")
    private PontoMonitorado ponto;

    /** Distância do sensor até o chão seco, em cm. */
    @Column(name = "altura_instalacao_cm")
    private double alturaInstalacaoCm;

    /** Nível de água, em cm, a partir do qual o risco é considerado ALTO. */
    @Column(name = "limite_alerta_cm")
    private double limiteAlertaCm;

    @Enumerated(EnumType.STRING)
    private StatusSensor status = StatusSensor.ATIVO;

    protected Sensor() {
    }

    public Sensor(String codigo, PontoMonitorado ponto, double alturaInstalacaoCm, double limiteAlertaCm,
                  StatusSensor status) {
        atualizar(codigo, ponto, alturaInstalacaoCm, limiteAlertaCm, status);
    }

    public void atualizar(String codigo, PontoMonitorado ponto, double alturaInstalacaoCm, double limiteAlertaCm,
                          StatusSensor status) {
        if (limiteAlertaCm > alturaInstalacaoCm) {
            throw new IllegalArgumentException("O limite de alerta não pode ser maior que a altura de instalação.");
        }
        this.codigo = codigo;
        this.ponto = ponto;
        this.alturaInstalacaoCm = alturaInstalacaoCm;
        this.limiteAlertaCm = limiteAlertaCm;
        this.status = status == null ? StatusSensor.ATIVO : status;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public PontoMonitorado getPonto() { return ponto; }
    public double getAlturaInstalacaoCm() { return alturaInstalacaoCm; }
    public double getLimiteAlertaCm() { return limiteAlertaCm; }
    public StatusSensor getStatus() { return status; }
}
