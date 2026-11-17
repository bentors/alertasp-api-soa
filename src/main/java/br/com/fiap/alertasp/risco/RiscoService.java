package br.com.fiap.alertasp.risco;

import br.com.fiap.alertasp.alerta.AlertaService;
import br.com.fiap.alertasp.alerta.OrigemAlerta;
import br.com.fiap.alertasp.ponto.PontoMonitorado;
import br.com.fiap.alertasp.ponto.PontoService;
import br.com.fiap.alertasp.risco.AvaliacaoRiscoDtos.AvaliacaoRiscoRequest;
import br.com.fiap.alertasp.shared.NivelRisco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Ponto único onde qualquer fonte (sensor, ML, ocorrência) altera o risco de um ponto e dispara alertas. */
@Service
public class RiscoService {

    private final AlertaService alertaService;
    private final PontoService pontoService;
    private final AvaliacaoRiscoRepository avaliacoes;

    public RiscoService(AlertaService alertaService, PontoService pontoService, AvaliacaoRiscoRepository avaliacoes) {
        this.alertaService = alertaService;
        this.pontoService = pontoService;
        this.avaliacoes = avaliacoes;
    }

    @Transactional
    public void aplicar(PontoMonitorado ponto, NivelRisco nivel, OrigemAlerta origem) {
        ponto.atualizarRisco(nivel);
        if (nivel.geraAlerta()) {
            alertaService.gerarParaPonto(ponto, nivel, origem);
        }
    }

    @Transactional
    public AvaliacaoRisco registrarAvaliacao(Long pontoId, AvaliacaoRiscoRequest req) {
        PontoMonitorado ponto = pontoService.buscar(pontoId);
        AvaliacaoRisco avaliacao = avaliacoes.save(
                new AvaliacaoRisco(ponto, req.nivel(), req.probabilidade(), req.modelo()));
        aplicar(ponto, req.nivel(), OrigemAlerta.MODELO_ML);
        return avaliacao;
    }

    public Page<AvaliacaoRisco> listarAvaliacoes(Long pontoId, Pageable pageable) {
        pontoService.buscar(pontoId);
        return avaliacoes.findByPontoIdOrderByCriadaEmDescIdDesc(pontoId, pageable);
    }
}
