package com.example.it_neocamp_projeto_final_workshop.dto.clube;

import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ClubePostRequest {
    @NotEmpty
    @Size(min = 2, message = "nome do clube deve ter pelo menos 2 caracteres")
    private String nome;
    @NotNull
    private EstadoBrasileiro estado;
    @PastOrPresent(message = "a data de criação deve ser hoje ou uma data passada")
    private LocalDate dataCriacao;
    @NotNull
    private Boolean ativo;
}
