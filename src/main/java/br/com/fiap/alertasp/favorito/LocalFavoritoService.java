package br.com.fiap.alertasp.favorito;

import br.com.fiap.alertasp.favorito.LocalFavoritoDtos.LocalFavoritoRequest;
import br.com.fiap.alertasp.shared.NaoEncontradoException;
import br.com.fiap.alertasp.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocalFavoritoService {

    private final LocalFavoritoRepository repository;
    private final UsuarioService usuarioService;

    public LocalFavoritoService(LocalFavoritoRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<LocalFavorito> listar(Long usuarioId) {
        usuarioService.buscar(usuarioId);
        return repository.findByUsuarioIdOrderByNome(usuarioId);
    }

    public LocalFavorito buscar(Long usuarioId, Long id) {
        return repository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("Local favorito", id));
    }

    @Transactional
    public LocalFavorito criar(Long usuarioId, LocalFavoritoRequest req) {
        var usuario = usuarioService.buscar(usuarioId);
        return repository.save(new LocalFavorito(usuario, req.nome(), req.localizacao(), req.raioOuPadrao()));
    }

    @Transactional
    public LocalFavorito atualizar(Long usuarioId, Long id, LocalFavoritoRequest req) {
        LocalFavorito favorito = buscar(usuarioId, id);
        favorito.atualizar(req.nome(), req.localizacao(), req.raioOuPadrao());
        return favorito;
    }

    @Transactional
    public void remover(Long usuarioId, Long id) {
        repository.delete(buscar(usuarioId, id));
    }
}
