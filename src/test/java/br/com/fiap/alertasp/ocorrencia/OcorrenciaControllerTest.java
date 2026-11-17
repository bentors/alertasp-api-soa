package br.com.fiap.alertasp.ocorrencia;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OcorrenciaControllerTest extends ApiTest {

    @Test
    void ocorrenciaOficial_marcaPontoAlto() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        postJson("/ocorrencias", """
                {"pontoId":%d,"localizacao":{"latitude":-23.5400,"longitude":-46.5700},
                 "descricao":"Alagamento confirmado pela Defesa Civil","fonte":"OFICIAL"}""".formatted(ponto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ABERTA"));
        mockMvc.perform(get("/pontos/" + ponto)).andExpect(jsonPath("$.nivelRiscoAtual").value("ALTO"));
    }

    @Test
    void ocorrenciaCidadao_marcaPontoMedio() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        postJson("/ocorrencias", """
                {"pontoId":%d,"localizacao":{"latitude":-23.5400,"longitude":-46.5700},
                 "descricao":"Água subindo na calçada","fonte":"CIDADAO"}""".formatted(ponto))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/pontos/" + ponto)).andExpect(jsonPath("$.nivelRiscoAtual").value("MEDIO"));
    }

    @Test
    void encerrarOcorrencia_filtraPorStatus() throws Exception {
        long id = criar("/ocorrencias", """
                {"localizacao":{"latitude":-23.53,"longitude":-46.60},"descricao":"Rua alagada","fonte":"CIDADAO"}""");
        mockMvc.perform(patch("/ocorrencias/" + id + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ENCERRADA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENCERRADA"));
        mockMvc.perform(get("/ocorrencias").param("status", "ABERTA"))
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
