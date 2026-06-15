package com.example.it_neocamp_projeto_final_workshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registro de novo usuário")
public record RegisterRequest(
        @NotBlank
        @Schema(description = "Nome de usuário único", example = "admin")
        String username,

        @NotBlank
        @Size(min = 6, message = "a senha deve ter no mínimo 6 caracteres")
        @Schema(description = "Senha com no mínimo 6 caracteres", example = "admin123", minLength = 6)
        String password
) {}
