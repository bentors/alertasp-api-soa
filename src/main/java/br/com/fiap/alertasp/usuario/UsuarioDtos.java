package br.com.fiap.alertasp.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTOs da API de usuários. */
public final class UsuarioDtos {

    private UsuarioDtos() {
    }

    public record UsuarioRequest(
            @Schema(example = "Maria Souza") @NotBlank @Size(max = 120) String nome,
            @Schema(example = "maria@email.com") @NotBlank @Email @Size(max = 150) String email) {
    }

    public record UsuarioResponse(Long id, String nome, String email) {

        static UsuarioResponse from(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail());
        }
    }
}
