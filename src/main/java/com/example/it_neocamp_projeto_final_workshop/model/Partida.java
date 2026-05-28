package com.example.it_neocamp_projeto_final_workshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne
    private Clube clubeCasa;
    @ManyToOne
    @NotNull
    private Clube clubeVisitante;
    private LocalDateTime dataHoraPartida;
}
