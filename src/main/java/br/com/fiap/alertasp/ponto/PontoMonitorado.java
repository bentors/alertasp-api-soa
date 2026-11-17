package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.shared.Coordenada;
import br.com.fiap.alertasp.shared.NivelRisco;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Local conhecido de risco de alagamento, monitorado por sensores, pelo modelo de ML e por ocorrências. */
@Entity
@Table(name = "ponto_monitorado")
public class PontoMonitorado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private String bairro;

    @Embedded
    private Coordenada localizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_risco_atual")
    private NivelRisco nivelRiscoAtual = NivelRisco.BAIXO;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected PontoMonitorado() {
    }

    public PontoMonitorado(String nome, String descricao, String bairro, Coordenada localizacao) {
        atualizarDados(nome, descricao, bairro, localizacao);
    }

    public void atualizarDados(String nome, String descricao, String bairro, Coordenada localizacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.bairro = bairro;
        this.localizacao = localizacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    // ponytail: o último evento define o risco; combinar fontes (máximo ou ponderado) se conflitarem na prática
    public void atualizarRisco(NivelRisco nivel) {
        this.nivelRiscoAtual = nivel;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getBairro() { return bairro; }
    public Coordenada getLocalizacao() { return localizacao; }
    public NivelRisco getNivelRiscoAtual() { return nivelRiscoAtual; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
