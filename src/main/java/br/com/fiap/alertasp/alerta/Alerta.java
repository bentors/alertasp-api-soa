package br.com.fiap.alertasp.alerta;

import br.com.fiap.alertasp.favorito.LocalFavorito;
import br.com.fiap.alertasp.ponto.PontoMonitorado;
import br.com.fiap.alertasp.rota.Rota;
import br.com.fiap.alertasp.shared.NivelRisco;
import br.com.fiap.alertasp.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Aviso ao cidadão de que um ponto de risco está próximo de uma rota ou local favorito dele. */
@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id")
    private PontoMonitorado ponto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rota_id")
    private Rota rota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_favorito_id")
    private LocalFavorito localFavorito;

    @Enumerated(EnumType.STRING)
    private NivelRisco nivel;

    @Enumerated(EnumType.STRING)
    private OrigemAlerta origem;

    private String mensagem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    private boolean lido;

    protected Alerta() {
    }

    public static Alerta paraRota(Rota rota, PontoMonitorado ponto, NivelRisco nivel, OrigemAlerta origem) {
        Alerta a = new Alerta(rota.getUsuario(), ponto, nivel, origem);
        a.rota = rota;
        a.mensagem = "Atenção! Risco %s de alagamento em %s, próximo ao seu trajeto \"%s\"."
                .formatted(nivel, ponto.getNome(), rota.getNome());
        return a;
    }

    public static Alerta paraFavorito(LocalFavorito favorito, PontoMonitorado ponto, NivelRisco nivel,
                                      OrigemAlerta origem) {
        Alerta a = new Alerta(favorito.getUsuario(), ponto, nivel, origem);
        a.localFavorito = favorito;
        a.mensagem = "Atenção! Risco %s de alagamento em %s, próximo de \"%s\"."
                .formatted(nivel, ponto.getNome(), favorito.getNome());
        return a;
    }

    private Alerta(Usuario usuario, PontoMonitorado ponto, NivelRisco nivel, OrigemAlerta origem) {
        this.usuario = usuario;
        this.ponto = ponto;
        this.nivel = nivel;
        this.origem = origem;
    }

    public void marcarComoLido() {
        this.lido = true;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public PontoMonitorado getPonto() { return ponto; }
    public Rota getRota() { return rota; }
    public LocalFavorito getLocalFavorito() { return localFavorito; }
    public NivelRisco getNivel() { return nivel; }
    public OrigemAlerta getOrigem() { return origem; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public boolean isLido() { return lido; }
}
