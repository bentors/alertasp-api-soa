package br.com.fiap.alertasp.usuario;

import br.com.fiap.alertasp.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest extends ApiTest {

    @Test
    void criarUsuario_retorna201() throws Exception {
        postJson("/usuarios", """
                {"nome":"Ana","email":"ana@teste.com"}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@teste.com"));
    }

    @Test
    void criarUsuario_emailDuplicado_retorna409() throws Exception {
        criarUsuario("dup@teste.com");
        postJson("/usuarios", """
                {"nome":"Outra","email":"dup@teste.com"}""")
                .andExpect(status().isConflict());
    }

    @Test
    void criarUsuario_emailInvalido_retorna400() throws Exception {
        postJson("/usuarios", """
                {"nome":"Ana","email":"nao-e-email"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.email").exists());
    }
}
