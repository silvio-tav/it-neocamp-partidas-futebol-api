package com.example.it_neocamp_projeto_final_workshop.dto.estadio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para cadastro ou atualização de um estádio")
public class EstadioRequest {

    @NotEmpty
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @Schema(description = "Nome do estádio", example = "Maracanã", minLength = 3)
    private String nome;
}
