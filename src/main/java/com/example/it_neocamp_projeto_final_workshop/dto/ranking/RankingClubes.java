package com.example.it_neocamp_projeto_final_workshop.dto.ranking;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Posição de um clube no ranking")
public record RankingClubes(
        @Schema(description = "ID do clube", example = "b1b2c3d4-0001-0000-0000-000000000001")
        UUID clubeId,

        @Schema(description = "Nome do clube", example = "Flamengo")
        String nome,

        @Schema(description = "Total de pontos (vitória = 3, empate = 1)", example = "9")
        long pontos,

        @Schema(description = "Total de gols marcados", example = "12")
        long gols,

        @Schema(description = "Total de vitórias", example = "3")
        long vitorias,

        @Schema(description = "Total de partidas disputadas", example = "4")
        long jogos
) { }
