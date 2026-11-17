package br.com.fiap.alertasp.shared;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI alertaSpOpenApi() {
        return new OpenAPI().info(new Info()
                .title("AlertaSP API")
                .version("1.0")
                .description("""
                        Backend do AlertaSP, uma CitizenTech que monitora o risco de alagamentos em São Paulo. \
                        Recebe medições de sensores de nível de água (ESP32), avaliações de risco de um modelo de \
                        Machine Learning e ocorrências, e alerta cidadãos quando um ponto de risco está próximo \
                        das rotas e locais favoritos que eles cadastraram (Minhas Rotas)."""));
    }
}
