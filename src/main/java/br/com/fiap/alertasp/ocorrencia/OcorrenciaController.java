package br.com.fiap.alertasp.ocorrencia;

import br.com.fiap.alertasp.ocorrencia.OcorrenciaDtos.OcorrenciaRequest;
import br.com.fiap.alertasp.ocorrencia.OcorrenciaDtos.OcorrenciaResponse;
import br.com.fiap.alertasp.ocorrencia.OcorrenciaDtos.StatusOcorrenciaRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ocorrencias")
@Tag(name = "Ocorrências", description = "Alagamentos reportados por cidadãos, sensores ou fontes oficiais")
public class OcorrenciaController {

    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra uma ocorrência de alagamento",
            description = "Se vinculada a um ponto, atualiza o risco: OFICIAL → ALTO (gera alertas); CIDADAO/SENSOR → MEDIO.")
    public OcorrenciaResponse registrar(@RequestBody @Valid OcorrenciaRequest req) {
        return OcorrenciaResponse.from(service.registrar(req));
    }

    @GetMapping
    @Operation(summary = "Lista ocorrências, opcionalmente filtrando por status")
    public Page<OcorrenciaResponse> listar(@RequestParam(required = false) Ocorrencia.Status status,
                                           Pageable pageable) {
        return service.listar(status, pageable).map(OcorrenciaResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma ocorrência")
    public OcorrenciaResponse buscar(@PathVariable Long id) {
        return OcorrenciaResponse.from(service.buscar(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Altera o status de uma ocorrência (ex.: encerrar)")
    public OcorrenciaResponse alterarStatus(@PathVariable Long id, @RequestBody @Valid StatusOcorrenciaRequest req) {
        return OcorrenciaResponse.from(service.alterarStatus(id, req.status()));
    }
}
