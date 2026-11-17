package br.com.fiap.alertasp.favorito;

import br.com.fiap.alertasp.shared.Coordenada;
import br.com.fiap.alertasp.usuario.Usuario;
import jakarta.persistence.*;

/** Local que o cidadão quer acompanhar (ex.: a rua de casa, a escola dos filhos). */
@Entity
@Table(name = "local_favorito")
public class LocalFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nome;

    @Embedded
    private Coordenada localizacao;

    @Column(name = "raio_alerta_metros")
    private int raioAlertaMetros;

    protected LocalFavorito() {
    }

    public LocalFavorito(Usuario usuario, String nome, Coordenada localizacao, int raioAlertaMetros) {
        this.usuario = usuario;
        atualizar(nome, localizacao, raioAlertaMetros);
    }

    public void atualizar(String nome, Coordenada localizacao, int raioAlertaMetros) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.raioAlertaMetros = raioAlertaMetros;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getNome() { return nome; }
    public Coordenada getLocalizacao() { return localizacao; }
    public int getRaioAlertaMetros() { return raioAlertaMetros; }
}
