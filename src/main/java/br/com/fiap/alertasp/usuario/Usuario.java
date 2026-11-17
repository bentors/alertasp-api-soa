package br.com.fiap.alertasp.usuario;

import jakarta.persistence.*;

/** Cidadão que cadastra rotas e locais favoritos para receber alertas. */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    protected Usuario() {
    }

    public Usuario(String nome, String email) {
        atualizar(nome, email);
    }

    public void atualizar(String nome, String email) {
        this.nome = nome;
        this.email = email.toLowerCase();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
}
