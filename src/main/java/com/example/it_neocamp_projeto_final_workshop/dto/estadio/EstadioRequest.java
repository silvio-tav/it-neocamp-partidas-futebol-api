package com.example.it_neocamp_projeto_final_workshop.dto.estadio;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EstadioRequest {
    @NotEmpty
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;
}
