package com.example.it_neocamp_projeto_final_workshop.dto.clube;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RetrospectoResponse {
    int totalJogos;
    int vitorias;
    int empates;
    int derrotas;
    int golsFeitos;
    int golsSofridos;
}
