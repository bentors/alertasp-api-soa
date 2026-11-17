package br.com.fiap.alertasp.alerta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Alertas", description = "Alertas de risco de alagamento próximos às rotas e favoritos do cidadão")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @GetMapping("/usuarios/{usuarioId}/alertas")
    @Operation(summary = "Lista os alertas do usuário (mais recentes primeiro), opcionalmente filtrando por lidos")
    public List<AlertaResponse> listar(@PathVariable Long usuarioId, @RequestParam(required = false) Boolean lido) {
        return service.listar(usuarioId, lido).stream().map(AlertaResponse::from).toList();
    }

    @PatchMapping("/alertas/{id}/lido")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Marca um alerta como lido")
    public void marcarComoLido(@PathVariable Long id) {
        service.marcarComoLido(id);
    }
}
