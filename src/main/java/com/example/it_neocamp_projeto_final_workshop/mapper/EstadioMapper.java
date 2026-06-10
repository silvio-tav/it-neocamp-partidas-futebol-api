package com.example.it_neocamp_projeto_final_workshop.mapper;

import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioResponse;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;

public class EstadioMapper {
    public static Estadio toEntity(EstadioRequest estadioRequest){
        return Estadio.builder()
                .nome(estadioRequest.getNome())
                .build();
    }

    public static EstadioResponse toResponse(Estadio estadio){
        return EstadioResponse.builder()
                .estadioId(estadio.getEstadioId())
                .nome(estadio.getNome())
                .build();
    }
}
