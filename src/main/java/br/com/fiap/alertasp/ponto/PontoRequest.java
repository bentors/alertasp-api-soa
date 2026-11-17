package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.shared.Coordenada;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PontoRequest(
        @Schema(example = "Ponte das Bandeiras") @NotBlank @Size(max = 120) String nome,
        @Schema(example = "Trecho da Marginal Tietê com histórico de alagamentos") @Size(max = 500) String descricao,
        @Schema(example = "Santana") @Size(max = 80) String bairro,
        @NotNull @Valid Coordenada localizacao) {
}
