package com.example.it_neocamp_projeto_final_workshop.dto.restrospecto;

import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class AdversarioPartidasRetrospecto {
    private List<PartidaResponse> partidas;
    private int totalJogos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsFeitos;
    private int golsSofridos;
}
