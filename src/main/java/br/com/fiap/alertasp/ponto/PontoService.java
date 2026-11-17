package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.shared.NaoEncontradoException;
import br.com.fiap.alertasp.shared.NivelRisco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class PontoService {

    private final PontoRepository repository;

    public PontoService(PontoRepository repository) {
        this.repository = repository;
    }

    public PontoMonitorado buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Ponto monitorado", id));
    }

    public Page<PontoMonitorado> listar(NivelRisco nivelMinimo, Pageable pageable) {
        if (nivelMinimo == null) {
            return repository.findAll(pageable);
        }
        var niveis = Arrays.stream(NivelRisco.values()).filter(n -> n.compareTo(nivelMinimo) >= 0).toList();
        return repository.findByNivelRiscoAtualIn(niveis, pageable);
    }

    @Transactional
    public PontoMonitorado criar(PontoRequest req) {
        return repository.save(new PontoMonitorado(req.nome(), req.descricao(), req.bairro(), req.localizacao()));
    }

    @Transactional
    public PontoMonitorado atualizar(Long id, PontoRequest req) {
        PontoMonitorado ponto = buscar(id);
        ponto.atualizarDados(req.nome(), req.descricao(), req.bairro(), req.localizacao());
        return ponto;
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
        repository.flush();
    }
}
