package com.example.it_neocamp_projeto_final_workshop.mapper;

import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaResponse;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;

public class PartidaMapper {

    public static PartidaResponse toResponse(Partida partida) {
        return PartidaResponse.builder()
                .id(partida.getId())
                .clubeCasa(ClubeMapper.toResponse(partida.getClubeCasa()))
                .clubeVisitante(ClubeMapper.toResponse(partida.getClubeVisitante()))
                .estadio(EstadioMapper.toResponse(partida.getEstadio()))
                .dataHoraPartida(partida.getDataHoraPartida())
                .golsCasa(partida.getGolsCasa())
                .golsVisitante(partida.getGolsVisitante())
                .build();
    }
}
