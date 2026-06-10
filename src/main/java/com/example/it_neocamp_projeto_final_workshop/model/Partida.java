package com.example.it_neocamp_projeto_final_workshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID partidaId;
    @ManyToOne
    @JoinColumn(name = "clube_casa_id")
    private Clube clubeCasa;
    @ManyToOne
    @JoinColumn(name = "clube_visitante_id")
    private Clube clubeVisitante;
    @ManyToOne
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;
    private LocalDateTime dataHoraPartida;
    private Integer golsCasa;
    private Integer golsVisitante;
}
