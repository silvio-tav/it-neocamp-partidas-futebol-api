package com.example.it_neocamp_projeto_final_workshop.dto.clube;

import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@Schema(description = "Dados do clube cadastrado")
public class ClubePostResponse {

    @Schema(description = "ID do clube cadastrado", example = "1")
    private Long id;

    @Schema(description = "Nome do clube", example = "Flamengo")
    private String nome;

    @Schema(description = "Sigla do estado brasileiro onde o clube está sediado", example = "RJ")
    private EstadoBrasileiro siglaEstado;

    @Schema(description = "Data de fundação do clube", example = "1895-11-15")
    private LocalDate dataCriacao;

    @Schema(description = "Indica se o clube está ativo", example = "true")
    private Boolean ativo;
}
