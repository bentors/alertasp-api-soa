package br.com.fiap.alertasp.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Base dos testes de API: contexto completo em H2, cada teste em transação revertida. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper json;

    protected ResultActions postJson(String url, String body) throws Exception {
        return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
    }

    protected long criar(String url, String body) throws Exception {
        String resposta = postJson(url, body).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = json.readTree(resposta);
        return node.get("id").asLong();
    }

    protected long criarPonto(double lat, double lng) throws Exception {
        return criar("/pontos", """
                {"nome":"Ponte Teste","descricao":"ponto de teste","bairro":"Tatuapé",
                 "localizacao":{"latitude":%s,"longitude":%s}}""".formatted(lat, lng));
    }

    protected long criarSensor(String codigo, long pontoId, double altura, double limite) throws Exception {
        return criar("/sensores", """
                {"codigo":"%s","pontoId":%d,"alturaInstalacaoCm":%s,"limiteAlertaCm":%s}"""
                .formatted(codigo, pontoId, altura, limite));
    }
}
