package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.shared.NivelRisco;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pontos")
@Tag(name = "Pontos monitorados", description = "Locais com risco de alagamento acompanhados pela plataforma")
public class PontoController {

    private final PontoService service;

    public PontoController(PontoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um ponto monitorado")
    public PontoResponse criar(@RequestBody @Valid PontoRequest req) {
        return PontoResponse.from(service.criar(req));
    }

    @GetMapping
    @Operation(summary = "Lista pontos monitorados, opcionalmente a partir de um nível mínimo de risco")
    public Page<PontoResponse> listar(@RequestParam(required = false) NivelRisco nivelMinimo, Pageable pageable) {
        return service.listar(nivelMinimo, pageable).map(PontoResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um ponto monitorado")
    public PontoResponse buscar(@PathVariable Long id) {
        return PontoResponse.from(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um ponto monitorado")
    public PontoResponse atualizar(@PathVariable Long id, @RequestBody @Valid PontoRequest req) {
        return PontoResponse.from(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um ponto monitorado (sem sensores vinculados)")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
