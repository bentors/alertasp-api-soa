package br.com.fiap.alertasp;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Casos de borda apontados na revisão final. */
class RevisaoFinalTest extends ApiTest {

    @Test
    void alertaComNomesNoTamanhoMaximo_eGeradoSemErro() throws Exception {
        String nomePonto = "P".repeat(120);
        String nomeRota = "R".repeat(120);
        long ponto = criar("/pontos", """
                {"nome":"%s","localizacao":{"latitude":-23.5400,"longitude":-46.5700}}""".formatted(nomePonto));
        long usuario = criarUsuario("nomes-longos@teste.com");
        criar("/usuarios/" + usuario + "/rotas", """
                {"nome":"%s","pontos":[{"latitude":-23.5395,"longitude":-46.5800},{"latitude":-23.5405,"longitude":-46.5600}]}"""
                .formatted(nomeRota));

        postJson("/pontos/" + ponto + "/avaliacoes-risco", """
                {"nivel":"CRITICO","probabilidade":0.99,"modelo":"random-forest-v1"}""")
                .andExpect(status().isCreated());

        mockMvc.perform(get("/usuarios/" + usuario + "/alertas"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void criarPonto_semLatitude_retorna400() throws Exception {
        postJson("/pontos", """
                {"nome":"Sem latitude","localizacao":{"longitude":-46.67}}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos['localizacao.latitude']").exists());
    }

    @Test
    void listarPontos_ordenacaoPorCampoInexistente_retorna400() throws Exception {
        mockMvc.perform(get("/pontos").param("sort", "string"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarMedicao_sensorEmManutencao_retorna409() throws Exception {
        long ponto = criarPonto(-23.5400, -46.5700);
        criar("/sensores", """
                {"codigo":"ESP32-MANUT","pontoId":%d,"alturaInstalacaoCm":200,"limiteAlertaCm":30,"status":"MANUTENCAO"}"""
                .formatted(ponto));

        postJson("/sensores/ESP32-MANUT/medicoes", "{\"distanciaCm\":150}")
                .andExpect(status().isConflict());
        mockMvc.perform(get("/pontos/" + ponto)).andExpect(jsonPath("$.nivelRiscoAtual").value("BAIXO"));
    }
}
