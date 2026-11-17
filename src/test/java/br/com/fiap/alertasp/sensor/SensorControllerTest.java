package br.com.fiap.alertasp.sensor;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SensorControllerTest extends ApiTest {

    @Test
    void criarSensor_codigoDuplicado_retorna409() throws Exception {
        long ponto = criarPonto(-23.51, -46.67);
        criarSensor("ESP32-T1", ponto, 200, 30);
        postJson("/sensores", """
                {"codigo":"ESP32-T1","pontoId":%d,"alturaInstalacaoCm":200,"limiteAlertaCm":30}""".formatted(ponto))
                .andExpect(status().isConflict());
    }

    @Test
    void criarSensor_limiteMaiorQueAltura_retorna400() throws Exception {
        long ponto = criarPonto(-23.51, -46.67);
        postJson("/sensores", """
                {"codigo":"ESP32-T2","pontoId":%d,"alturaInstalacaoCm":100,"limiteAlertaCm":150}""".formatted(ponto))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarMedicao_acimaDoLimite_marcaPontoAlto() throws Exception {
        long ponto = criarPonto(-23.51, -46.67);
        criarSensor("ESP32-T3", ponto, 200, 30);

        postJson("/sensores/ESP32-T3/medicoes", """
                {"distanciaCm":160}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nivelAguaCm").value(40.0))
                .andExpect(jsonPath("$.nivelRisco").value("ALTO"));

        mockMvc.perform(get("/pontos/" + ponto))
                .andExpect(jsonPath("$.nivelRiscoAtual").value("ALTO"));
    }

    @Test
    void registrarMedicao_sensorInexistente_retorna404() throws Exception {
        postJson("/sensores/NAO-EXISTE/medicoes", """
                {"distanciaCm":160}""")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("não encontrado")));
    }

    @Test
    void listarMedicoes_maisRecentePrimeiro() throws Exception {
        long ponto = criarPonto(-23.51, -46.67);
        long sensor = criarSensor("ESP32-T4", ponto, 200, 30);
        postJson("/sensores/ESP32-T4/medicoes", "{\"distanciaCm\":190}");
        postJson("/sensores/ESP32-T4/medicoes", "{\"distanciaCm\":180}");
        mockMvc.perform(get("/sensores/" + sensor + "/medicoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].distanciaCm").value(180.0));
    }
}
