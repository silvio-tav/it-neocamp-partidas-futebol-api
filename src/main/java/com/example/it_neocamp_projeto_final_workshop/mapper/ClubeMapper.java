package com.example.it_neocamp_projeto_final_workshop.mapper;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;

public class ClubeMapper {

    public static Clube toEntity(ClubePostRequest request) {
        return Clube.builder()
                .nome(request.getNome())
                .estado(request.getEstado())
                .dataCriacao(request.getDataCriacao())
                .ativo(request.getAtivo())
                .build();
    }
}
