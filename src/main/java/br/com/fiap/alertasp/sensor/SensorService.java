package br.com.fiap.alertasp.sensor;

import br.com.fiap.alertasp.alerta.OrigemAlerta;
import br.com.fiap.alertasp.ponto.PontoService;
import br.com.fiap.alertasp.risco.RiscoService;
import br.com.fiap.alertasp.sensor.SensorDtos.MedicaoRequest;
import br.com.fiap.alertasp.sensor.SensorDtos.SensorRequest;
import br.com.fiap.alertasp.shared.ConflitoException;
import br.com.fiap.alertasp.shared.NaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorService {

    private final SensorRepository sensores;
    private final MedicaoRepository medicoes;
    private final PontoService pontoService;
    private final RiscoService riscoService;

    public SensorService(SensorRepository sensores, MedicaoRepository medicoes, PontoService pontoService,
                         RiscoService riscoService) {
        this.sensores = sensores;
        this.medicoes = medicoes;
        this.pontoService = pontoService;
        this.riscoService = riscoService;
    }

    public Sensor buscar(Long id) {
        return sensores.findById(id).orElseThrow(() -> new NaoEncontradoException("Sensor", id));
    }

    public Page<Sensor> listar(Pageable pageable) {
        return sensores.findAll(pageable);
    }

    @Transactional
    public Sensor criar(SensorRequest req) {
        if (sensores.existsByCodigo(req.codigo())) {
            throw new ConflitoException("Já existe um sensor com o código " + req.codigo());
        }
        var ponto = pontoService.buscar(req.pontoId());
        return sensores.save(new Sensor(req.codigo(), ponto, req.alturaInstalacaoCm(), req.limiteAlertaCm(),
                req.status()));
    }

    @Transactional
    public Sensor atualizar(Long id, SensorRequest req) {
        Sensor sensor = buscar(id);
        if (!sensor.getCodigo().equals(req.codigo()) && sensores.existsByCodigo(req.codigo())) {
            throw new ConflitoException("Já existe um sensor com o código " + req.codigo());
        }
        sensor.atualizar(req.codigo(), pontoService.buscar(req.pontoId()), req.alturaInstalacaoCm(),
                req.limiteAlertaCm(), req.status());
        return sensor;
    }

    @Transactional
    public void remover(Long id) {
        sensores.delete(buscar(id));
    }

    @Transactional
    public Medicao registrarMedicao(String codigo, MedicaoRequest req) {
        Sensor sensor = sensores.findByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Sensor", codigo));
        if (sensor.getStatus() != StatusSensor.ATIVO) {
            throw new ConflitoException("Sensor " + codigo + " está " + sensor.getStatus()
                    + "; leituras só são aceitas de sensores ATIVO.");
        }
        Medicao medicao = medicoes.save(new Medicao(sensor, req.distanciaCm()));
        riscoService.aplicar(sensor.getPonto(), medicao.nivelRisco(), OrigemAlerta.SENSOR);
        return medicao;
    }

    public Page<Medicao> listarMedicoes(Long sensorId, Pageable pageable) {
        buscar(sensorId);
        return medicoes.findBySensorIdOrderByRegistradaEmDescIdDesc(sensorId, pageable);
    }
}
