package br.com.fiap.alertasp.alerta;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlertaFluxoTest extends ApiTest {

    static final String ROTA_SOBRE_O_PONTO =
            "[{\"latitude\":-23.5395,\"longitude\":-46.5800},{\"latitude\":-23.5405,\"longitude\":-46.5600}]";
    static final String ROTA_A_5KM =
            "[{\"latitude\":-23.5850,\"longitude\":-46.5800},{\"latitude\":-23.5850,\"longitude\":-46.5600}]";

    @Test
    void medicaoAcimaDoLimite_geraAlertaParaRotaProxima() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        criarSensor("ESP32-F1", ponto, 200, 30);
        long proximo = criarUsuario("proximo@teste.com");
        criarRota(proximo, ROTA_SOBRE_O_PONTO);
        long distante = criarUsuario("distante@teste.com");
        criarRota(distante, ROTA_A_5KM);

        postJson("/sensores/ESP32-F1/medicoes", "{\"distanciaCm\":150}");

        mockMvc.perform(get("/usuarios/" + proximo + "/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].origem").value("SENSOR"))
                .andExpect(jsonPath("$[0].nivel").value("ALTO"))
                .andExpect(jsonPath("$[0].mensagem").value(containsString("Casa → Trabalho")));
        mockMvc.perform(get("/usuarios/" + distante + "/alertas"))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void duasMedicoesSeguidas_geramUmUnicoAlerta() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        criarSensor("ESP32-F2", ponto, 200, 30);
        long usuario = criarUsuario("dedup@teste.com");
        criarRota(usuario, ROTA_SOBRE_O_PONTO);

        postJson("/sensores/ESP32-F2/medicoes", "{\"distanciaCm\":150}");
        postJson("/sensores/ESP32-F2/medicoes", "{\"distanciaCm\":140}");

        mockMvc.perform(get("/usuarios/" + usuario + "/alertas"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void avaliacaoMl_alto_geraAlertaParaFavoritoProximo() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        long usuario = criarUsuario("favml@teste.com");
        criar("/usuarios/" + usuario + "/favoritos", """
                {"nome":"Escola","localizacao":{"latitude":-23.5409,"longitude":-46.5700}}""");

        postJson("/pontos/" + ponto + "/avaliacoes-risco", """
                {"nivel":"ALTO","probabilidade":0.87,"modelo":"random-forest-v1"}""")
                .andExpect(status().isCreated());

        mockMvc.perform(get("/usuarios/" + usuario + "/alertas"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].origem").value("MODELO_ML"))
                .andExpect(jsonPath("$[0].mensagem").value(containsString("Escola")));
    }

    @Test
    void avaliacaoMl_medio_naoGeraAlerta() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        long usuario = criarUsuario("medio@teste.com");
        criarRota(usuario, ROTA_SOBRE_O_PONTO);

        postJson("/pontos/" + ponto + "/avaliacoes-risco", """
                {"nivel":"MEDIO","probabilidade":0.4,"modelo":"random-forest-v1"}""")
                .andExpect(status().isCreated());

        mockMvc.perform(get("/usuarios/" + usuario + "/alertas"))
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(get("/pontos/" + ponto)).andExpect(jsonPath("$.nivelRiscoAtual").value("MEDIO"));
    }

    @Test
    void avaliacao_probabilidadeForaDoIntervalo_retorna400() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        postJson("/pontos/" + ponto + "/avaliacoes-risco", """
                {"nivel":"ALTO","probabilidade":1.5,"modelo":"random-forest-v1"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.probabilidade").exists());
    }

    @Test
    void marcarAlertaLido_retorna204ESomeDosNaoLidos() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        criarSensor("ESP32-F3", ponto, 200, 30);
        long usuario = criarUsuario("lido@teste.com");
        criarRota(usuario, ROTA_SOBRE_O_PONTO);
        postJson("/sensores/ESP32-F3/medicoes", "{\"distanciaCm\":150}");
        String body = mockMvc.perform(get("/usuarios/" + usuario + "/alertas"))
                .andReturn().getResponse().getContentAsString();
        long alerta = json.readTree(body).get(0).get("id").asLong();

        mockMvc.perform(patch("/alertas/" + alerta + "/lido")).andExpect(status().isNoContent());

        mockMvc.perform(get("/usuarios/" + usuario + "/alertas").param("lido", "false"))
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(get("/usuarios/" + usuario + "/alertas").param("lido", "true"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
