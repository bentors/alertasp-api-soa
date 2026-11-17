package br.com.fiap.alertasp.usuario;

import br.com.fiap.alertasp.usuario.UsuarioDtos.UsuarioRequest;
import br.com.fiap.alertasp.usuario.UsuarioDtos.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Cidadãos que usam o aplicativo AlertaSP")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um usuário")
    public UsuarioResponse criar(@RequestBody @Valid UsuarioRequest req) {
        return UsuarioResponse.from(service.criar(req));
    }

    @GetMapping
    @Operation(summary = "Lista usuários")
    public Page<UsuarioResponse> listar(Pageable pageable) {
        return service.listar(pageable).map(UsuarioResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return UsuarioResponse.from(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequest req) {
        return UsuarioResponse.from(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um usuário com suas rotas, favoritos e alertas")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
