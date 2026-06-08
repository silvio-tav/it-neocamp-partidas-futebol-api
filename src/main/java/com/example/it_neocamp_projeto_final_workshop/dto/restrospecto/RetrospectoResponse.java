package com.example.it_neocamp_projeto_final_workshop.dto.restrospecto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(description = "Retrospecto de um clube em partidas")
public class RetrospectoResponse {

    @Schema(description = "Total de partidas disputadas", example = "10")
    private int totalJogos;

    @Schema(description = "Total de vitórias", example = "6")
    private int vitorias;

    @Schema(description = "Total de empates", example = "2")
    private int empates;

    @Schema(description = "Total de derrotas", example = "2")
    private int derrotas;

    @Schema(description = "Total de gols marcados", example = "18")
    private int golsFeitos;

    @Schema(description = "Total de gols sofridos", example = "10")
    private int golsSofridos;
}
