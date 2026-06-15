package com.example.it_neocamp_projeto_final_workshop.dto.partida;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para cadastro de uma partida")
public class PartidaPostRequest {

    @NotNull
    @Schema(description = "ID do clube mandante", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID clubeCasaId;

    @NotNull
    @Schema(description = "ID do clube visitante", example = "660e8400-e29b-41d4-a716-446655440001")
    private UUID clubeVisitanteId;

    @NotNull
    @Schema(description = "ID do estádio onde a partida ocorre", example = "770e8400-e29b-41d4-a716-446655440002")
    private UUID estadioId;

    @NotNull
    @PastOrPresent(message = "deve ser uma data no passado ou no presente")
    @Schema(description = "Data e hora da partida (hoje ou data futura)", example = "2026-06-15T16:00:00")
    private LocalDateTime dataHoraPartida;

    @Min(0)
    @NotNull
    @Schema(description = "Quantidade de gols do clube mandante", example = "2", minimum = "0")
    private Integer golsCasa;

    @Min(0)
    @NotNull
    @Schema(description = "Quantidade de gols do clube visitante", example = "1", minimum = "0")
    private Integer golsVisitante;
}
