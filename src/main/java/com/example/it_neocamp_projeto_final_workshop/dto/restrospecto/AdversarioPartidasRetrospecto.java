package com.example.it_neocamp_projeto_final_workshop.dto.restrospecto;

import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@Schema(description = "Confrontos diretos entre dois clubes e o retrospecto do clube principal neste confronto")
public class AdversarioPartidasRetrospecto {

    @Schema(description = "Lista de partidas disputadas entre os dois clubes")
    private List<PartidaResponse> partidas;

    @Schema(description = "Total de partidas disputadas entre os dois clubes", example = "3")
    private int totalJogos;

    @Schema(description = "Total de vitórias do clube principal neste confronto", example = "2")
    private int vitorias;

    @Schema(description = "Total de empates neste confronto", example = "0")
    private int empates;

    @Schema(description = "Total de derrotas do clube principal neste confronto", example = "1")
    private int derrotas;

    @Schema(description = "Total de gols marcados pelo clube principal neste confronto", example = "5")
    private int golsFeitos;

    @Schema(description = "Total de gols sofridos pelo clube principal neste confronto", example = "3")
    private int golsSofridos;
}
