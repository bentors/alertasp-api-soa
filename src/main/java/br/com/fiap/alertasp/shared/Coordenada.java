package br.com.fiap.alertasp.shared;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/** Ponto geográfico (graus decimais). Campos obrigatórios: uma coordenada ausente nunca vira (0, 0). */
@Embeddable
public record Coordenada(
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude) {
}
