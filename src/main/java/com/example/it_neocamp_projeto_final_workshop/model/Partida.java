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
    private UUID id;
    @ManyToOne
    private Clube clubeCasa;
    @ManyToOne
    private Clube clubeVisitante;
    @ManyToOne
    private Estadio estadio;
    private LocalDateTime dataHoraPartida;
    private Integer golsCasa;
    private Integer golsVisitante;
}
