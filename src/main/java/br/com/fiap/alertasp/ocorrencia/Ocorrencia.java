package br.com.fiap.alertasp.ocorrencia;

import br.com.fiap.alertasp.ponto.PontoMonitorado;
import br.com.fiap.alertasp.shared.Coordenada;
import br.com.fiap.alertasp.shared.NivelRisco;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Registro de alagamento observado, informado por cidadão, sensor ou fonte oficial. */
@Entity
@Table(name = "ocorrencia")
public class Ocorrencia {

    public enum Fonte {
        CIDADAO(NivelRisco.MEDIO), SENSOR(NivelRisco.MEDIO), OFICIAL(NivelRisco.ALTO);

        private final NivelRisco nivel;

        Fonte(NivelRisco nivel) {
            this.nivel = nivel;
        }

        /** Nível de risco que uma ocorrência desta fonte atribui ao ponto. */
        public NivelRisco nivel() {
            return nivel;
        }
    }

    public enum Status {
        ABERTA, ENCERRADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id")
    private PontoMonitorado ponto;

    @Embedded
    private Coordenada localizacao;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Fonte fonte;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ABERTA;

    @Column(name = "registrada_em")
    private LocalDateTime registradaEm = LocalDateTime.now();

    protected Ocorrencia() {
    }

    public Ocorrencia(PontoMonitorado ponto, Coordenada localizacao, String descricao, Fonte fonte) {
        this.ponto = ponto;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.fonte = fonte;
    }

    public void alterarStatus(Status status) {
        this.status = status;
    }

    public Long getId() { return id; }
    public PontoMonitorado getPonto() { return ponto; }
    public Coordenada getLocalizacao() { return localizacao; }
    public String getDescricao() { return descricao; }
    public Fonte getFonte() { return fonte; }
    public Status getStatus() { return status; }
    public LocalDateTime getRegistradaEm() { return registradaEm; }
}
