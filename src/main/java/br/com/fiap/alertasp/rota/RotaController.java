package br.com.fiap.alertasp.rota;

import br.com.fiap.alertasp.rota.RotaDtos.RotaRequest;
import br.com.fiap.alertasp.rota.RotaDtos.RotaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioId}/rotas")
@Tag(name = "Minhas Rotas", description = "Trajetos do cidadão monitorados para alertas de alagamento")
public class RotaController {

    private final RotaService service;

    public RotaController(RotaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma rota do usuário")
    public RotaResponse criar(@PathVariable Long usuarioId, @RequestBody @Valid RotaRequest req) {
        return RotaResponse.from(service.criar(usuarioId, req));
    }

    @GetMapping
    @Operation(summary = "Lista as rotas do usuário")
    public List<RotaResponse> listar(@PathVariable Long usuarioId) {
        return service.listar(usuarioId).stream().map(RotaResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma rota do usuário")
    public RotaResponse buscar(@PathVariable Long usuarioId, @PathVariable Long id) {
        return RotaResponse.from(service.buscar(usuarioId, id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma rota do usuário")
    public RotaResponse atualizar(@PathVariable Long usuarioId, @PathVariable Long id,
                                  @RequestBody @Valid RotaRequest req) {
        return RotaResponse.from(service.atualizar(usuarioId, id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove uma rota do usuário")
    public void remover(@PathVariable Long usuarioId, @PathVariable Long id) {
        service.remover(usuarioId, id);
    }
}
