package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.shared.Coordenada;
import br.com.fiap.alertasp.shared.NivelRisco;

import java.time.LocalDateTime;

public record PontoResponse(Long id, String nome, String descricao, String bairro, Coordenada localizacao,
                            NivelRisco nivelRiscoAtual, LocalDateTime atualizadoEm) {

    public static PontoResponse from(PontoMonitorado p) {
        return new PontoResponse(p.getId(), p.getNome(), p.getDescricao(), p.getBairro(), p.getLocalizacao(),
                p.getNivelRiscoAtual(), p.getAtualizadoEm());
    }
}
