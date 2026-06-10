package com.example.it_neocamp_projeto_final_workshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Partidas de Futebol")
                        .description("""
                                API REST para gerenciamento de partidas de futebol.

                                Permite cadastrar e consultar **clubes**, **estádios** e **partidas**, com as seguintes validações:
                                - Conflito de horário entre partidas (janela de 48h por clube)
                                - Disponibilidade do estádio no dia
                                - Situação (ativo/inativo) dos clubes

                                **Filtros disponíveis:**
                                - Partidas: por nome do clube, nome do estádio e goleadas (diferença ≥ 3 gols)
                                - Retrospecto: por atuação (`MANDANTE` ou `VISITANTE`)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IT Neocamp")
                                .email("silvio.tavares@mercadolivre.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente local")
                ))
                .tags(List.of(
                        new Tag().name("Clubes").description("Gerenciamento de clubes de futebol e seus retrospecto"),
                        new Tag().name("Estádios").description("Gerenciamento de estádios de futebol"),
                        new Tag().name("Partidas").description("Gerenciamento de partidas de futebol")
                ));
    }
}
