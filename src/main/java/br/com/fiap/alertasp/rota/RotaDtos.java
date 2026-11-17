package br.com.fiap.alertasp.rota;

import br.com.fiap.alertasp.shared.Coordenada;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.List;

/** DTOs da API de rotas (Minhas Rotas). */
public final class RotaDtos {

    private RotaDtos() {
    }

    public record RotaRequest(
            @Schema(example = "Casa → Trabalho") @NotBlank @Size(max = 120) String nome,
            @Schema(description = "Horário em que o usuário costuma sair", example = "07:30") LocalTime horarioSaida,
            @Schema(description = "Distância máxima (m) entre a rota e um ponto de risco para gerar alerta. Padrão: 300",
                    example = "300") @Min(50) @Max(5000) Integer raioAlertaMetros,
            @Schema(description = "Polilinha do trajeto calculada pelo app, em ordem (mínimo 2 pontos)")
            @NotNull @Size(min = 2, max = 500) List<@Valid @NotNull Coordenada> pontos) {

        int raioOuPadrao() {
            return raioAlertaMetros == null ? 300 : raioAlertaMetros;
        }
    }

    public record RotaResponse(Long id, Long usuarioId, String nome, LocalTime horarioSaida, int raioAlertaMetros,
                               List<Coordenada> pontos) {

        static RotaResponse from(Rota r) {
            return new RotaResponse(r.getId(), r.getUsuario().getId(), r.getNome(), r.getHorarioSaida(),
                    r.getRaioAlertaMetros(), List.copyOf(r.getPontos()));
        }
    }
}
