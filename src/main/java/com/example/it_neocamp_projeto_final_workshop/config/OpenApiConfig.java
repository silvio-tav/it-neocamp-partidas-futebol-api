package com.example.it_neocamp_projeto_final_workshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API partidas de Futebol")
                        .description("API de partidas de futebol," +
                                " para manusear dados de clubes, de partidas e de estádios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IT Neocamp")
                                .email("silvio.tavares@mercadolivre.com")));
    }
}
