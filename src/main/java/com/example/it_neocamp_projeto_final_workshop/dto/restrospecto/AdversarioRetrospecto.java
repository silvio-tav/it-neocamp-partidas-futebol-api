package com.example.it_neocamp_projeto_final_workshop.dto.restrospecto;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(description = "Retrospecto do clube contra um adversário específico")
public class AdversarioRetrospecto {

    @Schema(description = "Dados do clube adversário")
    private ClubeResponse adversario;

    @Schema(description = "Total de partidas disputadas contra este adversário", example = "5")
    private int totalJogos;

    @Schema(description = "Total de vitórias contra este adversário", example = "3")
    private int vitorias;

    @Schema(description = "Total de empates contra este adversário", example = "1")
    private int empates;

    @Schema(description = "Total de derrotas contra este adversário", example = "1")
    private int derrotas;

    @Schema(description = "Total de gols marcados contra este adversário", example = "8")
    private int golsFeitos;

    @Schema(description = "Total de gols sofridos contra este adversário", example = "4")
    private int golsSofridos;
}
