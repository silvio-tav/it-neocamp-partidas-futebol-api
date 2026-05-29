package com.example.it_neocamp_projeto_final_workshop.dto.partida;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
public class PartidaPostRequest {
    @NotNull
    private UUID clubeCasaId;
    @NotNull
    private UUID clubeVisitanteId;
    @NotNull
    private UUID estadioId;
    @NotNull
    @PastOrPresent
    private LocalDateTime dataHoraPartida;
    @Size
    @NotNull
    private Integer golsCasa;
    @Size
    @NotNull
    private Integer golsVisitante;
}
