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
                        .title("API de Partidas de Futebol")
                        .description("API REST para gerenciamento de partidas de futebol brasileiro. " +
                                "Permite cadastrar e consultar clubes, estádios e partidas, " +
                                "com validações de conflito de horário, disponibilidade de estádio e situação dos clubes.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IT Neocamp")
                                .email("silvio.tavares@mercadolivre.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
