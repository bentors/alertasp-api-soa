package br.com.fiap.alertasp.risco;

import br.com.fiap.alertasp.ponto.PontoMonitorado;
import br.com.fiap.alertasp.shared.NivelRisco;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Estimativa de risco de alagamento produzida pelo modelo de Machine Learning para um ponto. */
@Entity
@Table(name = "avaliacao_risco")
public class AvaliacaoRisco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id")
    private PontoMonitorado ponto;

    @Enumerated(EnumType.STRING)
    private NivelRisco nivel;

    private double probabilidade;

    private String modelo;

    @Column(name = "criada_em")
    private LocalDateTime criadaEm = LocalDateTime.now();

    protected AvaliacaoRisco() {
    }

    public AvaliacaoRisco(PontoMonitorado ponto, NivelRisco nivel, double probabilidade, String modelo) {
        this.ponto = ponto;
        this.nivel = nivel;
        this.probabilidade = probabilidade;
        this.modelo = modelo;
    }

    public Long getId() { return id; }
    public PontoMonitorado getPonto() { return ponto; }
    public NivelRisco getNivel() { return nivel; }
    public double getProbabilidade() { return probabilidade; }
    public String getModelo() { return modelo; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
}
