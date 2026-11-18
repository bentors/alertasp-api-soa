package br.com.fiap.alertasp.ponto;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PontoControllerTest extends ApiTest {

    @Test
    void criarPonto_retorna201ComNivelBaixo() throws Exception {
        postJson("/pontos", """
                {"nome":"Ponte do Limão","bairro":"Limão","localizacao":{"latitude":-23.5150,"longitude":-46.6700}}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nivelRiscoAtual").value("BAIXO"));
    }

    @Test
    void criarPonto_latitudeInvalida_retorna400() throws Exception {
        postJson("/pontos", """
                {"nome":"Inválido","localizacao":{"latitude":120,"longitude":-46.67}}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos['localizacao.latitude']").exists());
    }

    @Test
    void buscarPonto_inexistente_retorna404() throws Exception {
        mockMvc.perform(get("/pontos/999999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("não encontrado")));
    }

    @Test
    void listarPontos_filtraPorNivelMinimo() throws Exception {
        long baixo = criarPonto(-23.51, -46.67);
        mockMvc.perform(get("/pontos").param("nivelMinimo", "ALTO").param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.id == %d)]".formatted(baixo)).isEmpty())
                .andExpect(jsonPath("$.content[?(@.nivelRiscoAtual in ['BAIXO','MEDIO'])]").isEmpty());
    }
}
