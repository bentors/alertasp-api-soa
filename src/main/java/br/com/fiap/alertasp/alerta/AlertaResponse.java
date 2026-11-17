package br.com.fiap.alertasp.alerta;

import br.com.fiap.alertasp.shared.NivelRisco;

import java.time.LocalDateTime;

public record AlertaResponse(Long id, Long pontoId, String pontoNome, Long rotaId, Long localFavoritoId,
                             NivelRisco nivel, OrigemAlerta origem, String mensagem, LocalDateTime criadoEm,
                             boolean lido) {

    static AlertaResponse from(Alerta a) {
        return new AlertaResponse(a.getId(), a.getPonto().getId(), a.getPonto().getNome(),
                a.getRota() == null ? null : a.getRota().getId(),
                a.getLocalFavorito() == null ? null : a.getLocalFavorito().getId(),
                a.getNivel(), a.getOrigem(), a.getMensagem(), a.getCriadoEm(), a.isLido());
    }
}
