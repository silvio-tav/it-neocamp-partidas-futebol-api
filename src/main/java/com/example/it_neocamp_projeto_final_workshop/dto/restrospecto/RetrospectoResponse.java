package com.example.it_neocamp_projeto_final_workshop.dto.restrospecto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RetrospectoResponse {
    private int totalJogos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsFeitos;
    private int golsSofridos;
}
