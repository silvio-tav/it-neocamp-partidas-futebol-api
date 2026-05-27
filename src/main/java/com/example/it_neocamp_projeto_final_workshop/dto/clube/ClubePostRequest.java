package com.example.it_neocamp_projeto_final_workshop.dto.clube;

import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Dados para cadastro de um clube de futebol")
public class ClubePostRequest {

    @NotEmpty
    @Size(min = 2, message = "nome do clube deve ter pelo menos 2 caracteres")
    @Schema(description = "Nome do clube", example = "Flamengo", minLength = 2)
    private String nome;

    @NotNull
    @Schema(description = "Sigla do estado brasileiro onde o clube está sediado", example = "RJ")
    private EstadoBrasileiro estado;

    @PastOrPresent(message = "a data de criação deve ser hoje ou uma data passada")
    @Schema(description = "Data de fundação do clube (hoje ou data passada)", example = "1895-11-15")
    private LocalDate dataCriacao;

    @NotNull
    @Schema(description = "Indica se o clube está ativo", example = "true")
    private Boolean ativo;
}
