package br.com.fiap.alertasp.sensor;

import br.com.fiap.alertasp.shared.NivelRisco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** DTOs da API de sensores e medições. */
public final class SensorDtos {

    private SensorDtos() {
    }

    public record SensorRequest(
            @Schema(example = "ESP32-001") @NotBlank @Size(max = 50) String codigo,
            @Schema(example = "1") @NotNull Long pontoId,
            @Schema(description = "Distância do sensor até o chão seco (cm)", example = "200")
            @NotNull @Positive Double alturaInstalacaoCm,
            @Schema(description = "Nível de água (cm) a partir do qual o risco é ALTO", example = "30")
            @NotNull @Positive Double limiteAlertaCm,
            @Schema(description = "Padrão: ATIVO") StatusSensor status) {
    }

    public record SensorResponse(Long id, String codigo, Long pontoId, double alturaInstalacaoCm,
                                 double limiteAlertaCm, StatusSensor status) {

        static SensorResponse from(Sensor s) {
            return new SensorResponse(s.getId(), s.getCodigo(), s.getPonto().getId(), s.getAlturaInstalacaoCm(),
                    s.getLimiteAlertaCm(), s.getStatus());
        }
    }

    public record MedicaoRequest(
            @Schema(description = "Distância medida pelo sensor até a superfície (cm)", example = "150")
            @NotNull @PositiveOrZero Double distanciaCm) {
    }

    public record MedicaoResponse(Long id, Long sensorId, double distanciaCm, double nivelAguaCm,
                                  NivelRisco nivelRisco, LocalDateTime registradaEm) {

        static MedicaoResponse from(Medicao m) {
            return new MedicaoResponse(m.getId(), m.getSensor().getId(), m.getDistanciaCm(), m.getNivelAguaCm(),
                    m.nivelRisco(), m.getRegistradaEm());
        }
    }
}
