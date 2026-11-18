package br.com.fiap.alertasp.alerta;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Roda sem a transação de teste, como em produção (open-in-view desligado), para garantir que as
 * respostas não dependem de carregamento lazy depois que o serviço termina.
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class LeiturasForaDeTransacaoTest extends ApiTest {

    @Test
    void leiturasComRelacionamentos_naoFalhamSemSessaoAberta() throws Exception {
        long ponto = criarPonto(-23.6100, -46.7500);
        long sensor = criarSensor("ESP32-LAZY", ponto, 200, 30);
        long usuario = criarUsuario("lazy@teste.com");
        long rota = criarRota(usuario,
                "[{\"latitude\":-23.6095,\"longitude\":-46.7600},{\"latitude\":-23.6105,\"longitude\":-46.7400}]");
        postJson("/sensores/ESP32-LAZY/medicoes", "{\"distanciaCm\":150}").andExpect(status().isCreated());

        mockMvc.perform(get("/usuarios/" + usuario + "/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pontoNome").value("Ponte Teste"));
        mockMvc.perform(get("/usuarios/" + usuario + "/rotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pontos.length()").value(2));
        mockMvc.perform(get("/usuarios/" + usuario + "/rotas/" + rota))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pontos.length()").value(2));
        mockMvc.perform(get("/sensores/" + sensor + "/medicoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nivelRisco").value("ALTO"));
    }
}
