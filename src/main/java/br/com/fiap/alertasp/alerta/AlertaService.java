package br.com.fiap.alertasp.alerta;

import br.com.fiap.alertasp.favorito.LocalFavorito;
import br.com.fiap.alertasp.favorito.LocalFavoritoRepository;
import br.com.fiap.alertasp.ponto.PontoMonitorado;
import br.com.fiap.alertasp.rota.Rota;
import br.com.fiap.alertasp.rota.RotaRepository;
import br.com.fiap.alertasp.shared.GeoUtils;
import br.com.fiap.alertasp.shared.NaoEncontradoException;
import br.com.fiap.alertasp.shared.NivelRisco;
import br.com.fiap.alertasp.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlertaService {

    /** Janela em que um novo alerta para o mesmo usuário e ponto é suprimido enquanto o anterior não foi lido. */
    static final Duration JANELA_DEDUPLICACAO = Duration.ofHours(2);

    private final AlertaRepository alertas;
    private final RotaRepository rotas;
    private final LocalFavoritoRepository favoritos;
    private final UsuarioService usuarioService;

    public AlertaService(AlertaRepository alertas, RotaRepository rotas, LocalFavoritoRepository favoritos,
                         UsuarioService usuarioService) {
        this.alertas = alertas;
        this.rotas = rotas;
        this.favoritos = favoritos;
        this.usuarioService = usuarioService;
    }

    /**
     * Gera alertas para os usuários com rota ou local favorito dentro do raio de alerta do ponto.
     * No máximo um alerta não lido por usuário e ponto dentro da janela de deduplicação.
     */
    // ponytail: varre todas as rotas e favoritos em memória; PostGIS + índice espacial se o volume crescer
    @Transactional
    public List<Alerta> gerarParaPonto(PontoMonitorado ponto, NivelRisco nivel, OrigemAlerta origem) {
        var desde = LocalDateTime.now().minus(JANELA_DEDUPLICACAO);
        Set<Long> avisados = new HashSet<>();
        List<Alerta> gerados = new ArrayList<>();

        for (Rota rota : rotas.findAll()) {
            if (GeoUtils.distanciaAteLinhaMetros(ponto.getLocalizacao(), rota.getPontos()) <= rota.getRaioAlertaMetros()
                    && podeAvisar(rota.getUsuario().getId(), ponto, desde, avisados)) {
                gerados.add(alertas.save(Alerta.paraRota(rota, ponto, nivel, origem)));
            }
        }
        for (LocalFavorito favorito : favoritos.findAll()) {
            if (GeoUtils.distanciaMetros(ponto.getLocalizacao(), favorito.getLocalizacao()) <= favorito.getRaioAlertaMetros()
                    && podeAvisar(favorito.getUsuario().getId(), ponto, desde, avisados)) {
                gerados.add(alertas.save(Alerta.paraFavorito(favorito, ponto, nivel, origem)));
            }
        }
        return gerados;
    }

    private boolean podeAvisar(Long usuarioId, PontoMonitorado ponto, LocalDateTime desde, Set<Long> avisados) {
        return avisados.add(usuarioId)
                && !alertas.existsByUsuarioIdAndPontoIdAndLidoFalseAndCriadoEmAfter(usuarioId, ponto.getId(), desde);
    }

    public List<Alerta> listar(Long usuarioId, Boolean lido) {
        usuarioService.buscar(usuarioId);
        return lido == null
                ? alertas.findByUsuarioIdOrderByCriadoEmDescIdDesc(usuarioId)
                : alertas.findByUsuarioIdAndLidoOrderByCriadoEmDescIdDesc(usuarioId, lido);
    }

    @Transactional
    public void marcarComoLido(Long id) {
        alertas.findById(id).orElseThrow(() -> new NaoEncontradoException("Alerta", id)).marcarComoLido();
    }
}
