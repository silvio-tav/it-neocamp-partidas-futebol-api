package com.example.it_neocamp_projeto_final_workshop.dto.clube;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ClubePostRequest {
    @NotEmpty
    private String nome;
    @NotEmpty
    private String estado;
    @PastOrPresent(message = "a data de criação deve ser hoje ou uma data passada")
    private LocalDate dataCriacao;
    @NotNull
    private Boolean ativo;
}
