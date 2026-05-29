package com.example.it_neocamp_projeto_final_workshop.dto.partida;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartidaPutRequest {
    private UUID clubeCasaId;
    private UUID clubeVisitanteId;
    private UUID estadioId;
    @PastOrPresent
    private LocalDateTime dataHoraPartida;
    @Min(0)
    private Integer golsCasa;
    @Min(0)
    private Integer golsVisitante;
}
