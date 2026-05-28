package com.example.it_neocamp_projeto_final_workshop.dto.estadio;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EstadioResponse {
    private Long id;
    private String nome;
}
