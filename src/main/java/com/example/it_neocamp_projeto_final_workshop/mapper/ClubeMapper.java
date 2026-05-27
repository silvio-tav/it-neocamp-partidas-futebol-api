package com.example.it_neocamp_projeto_final_workshop.mapper;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubeResponse;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;

public class ClubeMapper {
    public static Clube toEntity(ClubePostRequest request) {
        return Clube.builder()
                .nome(request.getNome())
                .siglaEstado(request.getEstado())
                .dataCriacao(request.getDataCriacao())
                .ativo(true)
                .build();
    }

    public static ClubeResponse toResponse(Clube clube) {
        return ClubeResponse.builder()
                .id(clube.getId())
                .nome(clube.getNome())
                .siglaEstado(clube.getSiglaEstado())
                .dataCriacao(clube.getDataCriacao())
                .ativo(clube.getAtivo())
                .build();
    }
}
