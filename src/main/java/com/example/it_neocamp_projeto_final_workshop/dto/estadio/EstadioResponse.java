package com.example.it_neocamp_projeto_final_workshop.dto.estadio;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class EstadioResponse {
    private UUID id;
    private String nome;
}
