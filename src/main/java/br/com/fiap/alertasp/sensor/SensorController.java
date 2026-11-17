package br.com.fiap.alertasp.sensor;

import br.com.fiap.alertasp.sensor.SensorDtos.MedicaoRequest;
import br.com.fiap.alertasp.sensor.SensorDtos.MedicaoResponse;
import br.com.fiap.alertasp.sensor.SensorDtos.SensorRequest;
import br.com.fiap.alertasp.sensor.SensorDtos.SensorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensores")
@Tag(name = "Sensores e medições", description = "Sensores de nível de água (ESP32) e as leituras que enviam")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um sensor em um ponto monitorado")
    public SensorResponse criar(@RequestBody @Valid SensorRequest req) {
        return SensorResponse.from(service.criar(req));
    }

    @GetMapping
    @Operation(summary = "Lista sensores")
    public Page<SensorResponse> listar(Pageable pageable) {
        return service.listar(pageable).map(SensorResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um sensor")
    public SensorResponse buscar(@PathVariable Long id) {
        return SensorResponse.from(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um sensor")
    public SensorResponse atualizar(@PathVariable Long id, @RequestBody @Valid SensorRequest req) {
        return SensorResponse.from(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um sensor e suas medições")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }

    @PostMapping("/{codigo}/medicoes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra uma medição enviada pelo sensor (endpoint chamado pelo ESP32)",
            description = "Calcula o nível de água e atualiza o risco do ponto; risco ALTO dispara alertas.")
    public MedicaoResponse registrarMedicao(@PathVariable String codigo, @RequestBody @Valid MedicaoRequest req) {
        return MedicaoResponse.from(service.registrarMedicao(codigo, req));
    }

    @GetMapping("/{id}/medicoes")
    @Operation(summary = "Histórico de medições de um sensor (mais recentes primeiro)")
    public Page<MedicaoResponse> listarMedicoes(@PathVariable Long id, Pageable pageable) {
        return service.listarMedicoes(id, pageable).map(MedicaoResponse::from);
    }
}
