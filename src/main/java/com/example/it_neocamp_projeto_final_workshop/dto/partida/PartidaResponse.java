package com.example.it_neocamp_projeto_final_workshop.dto.partida;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubeResponse;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Dados da partida cadastrada")
public class PartidaResponse {

    @Schema(description = "ID da partida", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Clube mandante")
    private ClubeResponse clubeCasa;

    @Schema(description = "Clube visitante")
    private ClubeResponse clubeVisitante;

    @Schema(description = "Estádio onde a partida ocorre")
    private EstadioResponse estadio;

    @Schema(description = "Data e hora da partida", example = "2024-06-01T16:00:00")
    private LocalDateTime dataHoraPartida;

    @Schema(description = "Gols do clube mandante", example = "2")
    private Integer golsCasa;

    @Schema(description = "Gols do clube visitante", example = "1")
    private Integer golsVisitante;
}
