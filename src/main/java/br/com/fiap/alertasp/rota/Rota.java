package br.com.fiap.alertasp.rota;

import br.com.fiap.alertasp.shared.Coordenada;
import br.com.fiap.alertasp.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Trajeto cotidiano do cidadão (ex.: Casa → Trabalho), guardado como uma polilinha de coordenadas. */
@Entity
@Table(name = "rota")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nome;

    @Column(name = "horario_saida")
    private LocalTime horarioSaida;

    @Column(name = "raio_alerta_metros")
    private int raioAlertaMetros;

    @ElementCollection
    @CollectionTable(name = "rota_coordenada", joinColumns = @JoinColumn(name = "rota_id"))
    @OrderColumn(name = "ordem")
    private List<Coordenada> pontos = new ArrayList<>();

    protected Rota() {
    }

    public Rota(Usuario usuario, String nome, LocalTime horarioSaida, int raioAlertaMetros, List<Coordenada> pontos) {
        this.usuario = usuario;
        atualizar(nome, horarioSaida, raioAlertaMetros, pontos);
    }

    public void atualizar(String nome, LocalTime horarioSaida, int raioAlertaMetros, List<Coordenada> pontos) {
        this.nome = nome;
        this.horarioSaida = horarioSaida;
        this.raioAlertaMetros = raioAlertaMetros;
        this.pontos.clear();
        this.pontos.addAll(pontos);
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getNome() { return nome; }
    public LocalTime getHorarioSaida() { return horarioSaida; }
    public int getRaioAlertaMetros() { return raioAlertaMetros; }
    public List<Coordenada> getPontos() { return pontos; }
}
