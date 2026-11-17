package br.com.fiap.alertasp.favorito;

import br.com.fiap.alertasp.favorito.LocalFavoritoDtos.LocalFavoritoRequest;
import br.com.fiap.alertasp.favorito.LocalFavoritoDtos.LocalFavoritoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioId}/favoritos")
@Tag(name = "Locais favoritos", description = "Locais acompanhados pelo cidadão (casa, escola, trabalho...)")
public class LocalFavoritoController {

    private final LocalFavoritoService service;

    public LocalFavoritoController(LocalFavoritoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um local favorito do usuário")
    public LocalFavoritoResponse criar(@PathVariable Long usuarioId, @RequestBody @Valid LocalFavoritoRequest req) {
        return LocalFavoritoResponse.from(service.criar(usuarioId, req));
    }

    @GetMapping
    @Operation(summary = "Lista os locais favoritos do usuário")
    public List<LocalFavoritoResponse> listar(@PathVariable Long usuarioId) {
        return service.listar(usuarioId).stream().map(LocalFavoritoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um local favorito do usuário")
    public LocalFavoritoResponse buscar(@PathVariable Long usuarioId, @PathVariable Long id) {
        return LocalFavoritoResponse.from(service.buscar(usuarioId, id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um local favorito do usuário")
    public LocalFavoritoResponse atualizar(@PathVariable Long usuarioId, @PathVariable Long id,
                                           @RequestBody @Valid LocalFavoritoRequest req) {
        return LocalFavoritoResponse.from(service.atualizar(usuarioId, id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um local favorito do usuário")
    public void remover(@PathVariable Long usuarioId, @PathVariable Long id) {
        service.remover(usuarioId, id);
    }
}
