package br.com.fiap.alertasp.rota;

import br.com.fiap.alertasp.rota.RotaDtos.RotaRequest;
import br.com.fiap.alertasp.shared.NaoEncontradoException;
import br.com.fiap.alertasp.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RotaService {

    private final RotaRepository repository;
    private final UsuarioService usuarioService;

    public RotaService(RotaRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<Rota> listar(Long usuarioId) {
        usuarioService.buscar(usuarioId);
        return repository.findByUsuarioIdOrderByNome(usuarioId);
    }

    public Rota buscar(Long usuarioId, Long id) {
        return repository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("Rota", id));
    }

    @Transactional
    public Rota criar(Long usuarioId, RotaRequest req) {
        var usuario = usuarioService.buscar(usuarioId);
        return repository.save(new Rota(usuario, req.nome(), req.horarioSaida(), req.raioOuPadrao(), req.pontos()));
    }

    @Transactional
    public Rota atualizar(Long usuarioId, Long id, RotaRequest req) {
        Rota rota = buscar(usuarioId, id);
        rota.atualizar(req.nome(), req.horarioSaida(), req.raioOuPadrao(), req.pontos());
        return rota;
    }

    @Transactional
    public void remover(Long usuarioId, Long id) {
        repository.delete(buscar(usuarioId, id));
    }
}
