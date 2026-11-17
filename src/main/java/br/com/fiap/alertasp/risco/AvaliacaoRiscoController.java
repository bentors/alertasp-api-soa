package br.com.fiap.alertasp.risco;

import br.com.fiap.alertasp.risco.AvaliacaoRiscoDtos.AvaliacaoRiscoRequest;
import br.com.fiap.alertasp.risco.AvaliacaoRiscoDtos.AvaliacaoRiscoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pontos/{pontoId}/avaliacoes-risco")
@Tag(name = "Avaliações de risco (ML)", description = "Estimativas de risco enviadas pelo modelo de Machine Learning")
public class AvaliacaoRiscoController {

    private final RiscoService service;

    public AvaliacaoRiscoController(RiscoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra uma avaliação de risco do modelo de ML (endpoint chamado pelo serviço de ML)",
            description = "Atualiza o risco do ponto; risco ALTO ou CRITICO dispara alertas.")
    public AvaliacaoRiscoResponse registrar(@PathVariable Long pontoId, @RequestBody @Valid AvaliacaoRiscoRequest req) {
        return AvaliacaoRiscoResponse.from(service.registrarAvaliacao(pontoId, req));
    }

    @GetMapping
    @Operation(summary = "Histórico de avaliações de risco do ponto (mais recentes primeiro)")
    public Page<AvaliacaoRiscoResponse> listar(@PathVariable Long pontoId, Pageable pageable) {
        return service.listarAvaliacoes(pontoId, pageable).map(AvaliacaoRiscoResponse::from);
    }
}
