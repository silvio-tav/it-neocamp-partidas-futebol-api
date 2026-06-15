package com.example.it_neocamp_projeto_final_workshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação")
public record LoginRequest(
        @NotBlank
        @Schema(description = "Nome de usuário", example = "admin")
        String username,

        @NotBlank
        @Schema(description = "Senha do usuário", example = "admin123")
        String password
) {}
