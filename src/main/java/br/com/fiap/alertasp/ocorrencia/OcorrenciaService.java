package br.com.fiap.alertasp.ocorrencia;

import br.com.fiap.alertasp.alerta.OrigemAlerta;
import br.com.fiap.alertasp.ocorrencia.OcorrenciaDtos.OcorrenciaRequest;
import br.com.fiap.alertasp.ponto.PontoMonitorado;
import br.com.fiap.alertasp.ponto.PontoService;
import br.com.fiap.alertasp.risco.RiscoService;
import br.com.fiap.alertasp.shared.NaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository repository;
    private final PontoService pontoService;
    private final RiscoService riscoService;

    public OcorrenciaService(OcorrenciaRepository repository, PontoService pontoService, RiscoService riscoService) {
        this.repository = repository;
        this.pontoService = pontoService;
        this.riscoService = riscoService;
    }

    public Ocorrencia buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Ocorrência", id));
    }

    public Page<Ocorrencia> listar(Ocorrencia.Status status, Pageable pageable) {
        return status == null ? repository.findAll(pageable) : repository.findByStatus(status, pageable);
    }

    @Transactional
    public Ocorrencia registrar(OcorrenciaRequest req) {
        PontoMonitorado ponto = req.pontoId() == null ? null : pontoService.buscar(req.pontoId());
        Ocorrencia ocorrencia = repository.save(
                new Ocorrencia(ponto, req.localizacao(), req.descricao(), req.fonte()));
        if (ponto != null) {
            riscoService.aplicar(ponto, req.fonte().nivel(), OrigemAlerta.OCORRENCIA);
        }
        return ocorrencia;
    }

    @Transactional
    public Ocorrencia alterarStatus(Long id, Ocorrencia.Status status) {
        Ocorrencia ocorrencia = buscar(id);
        ocorrencia.alterarStatus(status);
        return ocorrencia;
    }
}
