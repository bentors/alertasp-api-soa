package br.com.fiap.alertasp.rota;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RotaControllerTest extends ApiTest {

    @Test
    void criarRota_retorna201ComPontosEmOrdem() throws Exception {
        long usuario = criarUsuario("rota@teste.com");
        postJson("/usuarios/" + usuario + "/rotas", """
                {"nome":"Casa → Trabalho","horarioSaida":"07:30","raioAlertaMetros":300,
                 "pontos":[{"latitude":-23.540,"longitude":-46.576},
                           {"latitude":-23.550,"longitude":-46.620},
                           {"latitude":-23.561,"longitude":-46.656}]}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pontos.length()").value(3))
                .andExpect(jsonPath("$.pontos[0].longitude").value(-46.576))
                .andExpect(jsonPath("$.pontos[2].longitude").value(-46.656))
                .andExpect(jsonPath("$.horarioSaida").value("07:30:00"));
    }

    @Test
    void criarRota_comUmPonto_retorna400() throws Exception {
        long usuario = criarUsuario("umponto@teste.com");
        postJson("/usuarios/" + usuario + "/rotas", """
                {"nome":"Curta","pontos":[{"latitude":-23.54,"longitude":-46.57}]}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.pontos").exists());
    }

    @Test
    void criarRota_raioPadrao300() throws Exception {
        long usuario = criarUsuario("raio@teste.com");
        postJson("/usuarios/" + usuario + "/rotas", """
                {"nome":"Sem raio","pontos":[{"latitude":-23.54,"longitude":-46.57},{"latitude":-23.55,"longitude":-46.58}]}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.raioAlertaMetros").value(300));
    }

    @Test
    void buscarRota_deOutroUsuario_retorna404() throws Exception {
        long dono = criarUsuario("dono@teste.com");
        long outro = criarUsuario("outro@teste.com");
        long rota = criarRota(dono, "[{\"latitude\":-23.54,\"longitude\":-46.57},{\"latitude\":-23.55,\"longitude\":-46.58}]");
        mockMvc.perform(get("/usuarios/" + outro + "/rotas/" + rota)).andExpect(status().isNotFound());
        mockMvc.perform(get("/usuarios/" + dono + "/rotas/" + rota)).andExpect(status().isOk());
    }

    @Test
    void criarFavorito_retorna201() throws Exception {
        long usuario = criarUsuario("fav@teste.com");
        postJson("/usuarios/" + usuario + "/favoritos", """
                {"nome":"Casa","localizacao":{"latitude":-23.540,"longitude":-46.576}}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Casa"))
                .andExpect(jsonPath("$.raioAlertaMetros").value(300));
        mockMvc.perform(get("/usuarios/" + usuario + "/favoritos"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
