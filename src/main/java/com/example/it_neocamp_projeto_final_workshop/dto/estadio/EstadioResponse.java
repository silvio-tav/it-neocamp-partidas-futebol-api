package com.example.it_neocamp_projeto_final_workshop.dto.estadio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@Schema(description = "Dados do estádio cadastrado")
public class EstadioResponse {

    @Schema(description = "ID do estádio", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nome do estádio", example = "Maracanã")
    private String nome;
}
