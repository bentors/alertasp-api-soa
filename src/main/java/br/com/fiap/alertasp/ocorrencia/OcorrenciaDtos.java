package br.com.fiap.alertasp.ocorrencia;

import br.com.fiap.alertasp.shared.Coordenada;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** DTOs da API de ocorrências. */
public final class OcorrenciaDtos {

    private OcorrenciaDtos() {
    }

    public record OcorrenciaRequest(
            @Schema(description = "Ponto monitorado relacionado, se houver", example = "1") Long pontoId,
            @NotNull @Valid Coordenada localizacao,
            @Schema(example = "Água cobrindo a faixa da direita") @NotBlank @Size(max = 500) String descricao,
            @Schema(example = "CIDADAO") @NotNull Ocorrencia.Fonte fonte) {
    }

    public record StatusOcorrenciaRequest(@Schema(example = "ENCERRADA") @NotNull Ocorrencia.Status status) {
    }

    public record OcorrenciaResponse(Long id, Long pontoId, Coordenada localizacao, String descricao,
                                     Ocorrencia.Fonte fonte, Ocorrencia.Status status, LocalDateTime registradaEm) {

        static OcorrenciaResponse from(Ocorrencia o) {
            return new OcorrenciaResponse(o.getId(), o.getPonto() == null ? null : o.getPonto().getId(),
                    o.getLocalizacao(), o.getDescricao(), o.getFonte(), o.getStatus(), o.getRegistradaEm());
        }
    }
}
