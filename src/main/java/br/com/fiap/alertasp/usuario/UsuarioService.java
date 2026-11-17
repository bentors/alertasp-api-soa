package br.com.fiap.alertasp.usuario;

import br.com.fiap.alertasp.shared.ConflitoException;
import br.com.fiap.alertasp.shared.NaoEncontradoException;
import br.com.fiap.alertasp.usuario.UsuarioDtos.UsuarioRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Usuário", id));
    }

    public Page<Usuario> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public Usuario criar(UsuarioRequest req) {
        exigirEmailLivre(req.email());
        return repository.save(new Usuario(req.nome(), req.email()));
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequest req) {
        Usuario usuario = buscar(id);
        if (!usuario.getEmail().equalsIgnoreCase(req.email())) {
            exigirEmailLivre(req.email());
        }
        usuario.atualizar(req.nome(), req.email());
        return usuario;
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
    }

    private void exigirEmailLivre(String email) {
        if (repository.existsByEmail(email.toLowerCase())) {
            throw new ConflitoException("Já existe um usuário com o e-mail " + email);
        }
    }
}
