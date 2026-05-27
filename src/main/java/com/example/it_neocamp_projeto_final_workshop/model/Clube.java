package com.example.it_neocamp_projeto_final_workshop.model;

import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Clube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private EstadoBrasileiro siglaEstado;
    private LocalDate dataCriacao;
    private Boolean ativo;
}
