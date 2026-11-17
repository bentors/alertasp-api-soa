package br.com.fiap.alertasp.favorito;

import br.com.fiap.alertasp.shared.Coordenada;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTOs da API de locais favoritos. */
public final class LocalFavoritoDtos {

    private LocalFavoritoDtos() {
    }

    public record LocalFavoritoRequest(
            @Schema(example = "Casa") @NotBlank @Size(max = 120) String nome,
            @NotNull @Valid Coordenada localizacao,
            @Schema(description = "Padrão: 300", example = "300") @Min(50) @Max(5000) Integer raioAlertaMetros) {

        int raioOuPadrao() {
            return raioAlertaMetros == null ? 300 : raioAlertaMetros;
        }
    }

    public record LocalFavoritoResponse(Long id, Long usuarioId, String nome, Coordenada localizacao,
                                        int raioAlertaMetros) {

        static LocalFavoritoResponse from(LocalFavorito f) {
            return new LocalFavoritoResponse(f.getId(), f.getUsuario().getId(), f.getNome(), f.getLocalizacao(),
                    f.getRaioAlertaMetros());
        }
    }
}
