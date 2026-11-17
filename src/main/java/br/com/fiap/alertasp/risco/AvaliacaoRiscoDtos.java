package br.com.fiap.alertasp.risco;

import br.com.fiap.alertasp.shared.NivelRisco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** DTOs da API de avaliações de risco (integração com o modelo de ML). */
public final class AvaliacaoRiscoDtos {

    private AvaliacaoRiscoDtos() {
    }

    public record AvaliacaoRiscoRequest(
            @Schema(example = "ALTO") @NotNull NivelRisco nivel,
            @Schema(description = "Probabilidade estimada de alagamento (0 a 1)", example = "0.87")
            @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double probabilidade,
            @Schema(example = "random-forest-v1") @NotBlank @Size(max = 80) String modelo) {
    }

    public record AvaliacaoRiscoResponse(Long id, Long pontoId, NivelRisco nivel, double probabilidade,
                                         String modelo, LocalDateTime criadaEm) {

        static AvaliacaoRiscoResponse from(AvaliacaoRisco a) {
            return new AvaliacaoRiscoResponse(a.getId(), a.getPonto().getId(), a.getNivel(), a.getProbabilidade(),
                    a.getModelo(), a.getCriadaEm());
        }
    }
}
