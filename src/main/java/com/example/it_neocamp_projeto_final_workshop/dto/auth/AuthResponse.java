package com.example.it_neocamp_projeto_final_workshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo o token JWT")
public record AuthResponse(
        @Schema(description = "Token JWT a ser enviado no header Authorization: Bearer <token>",
                example = "eyJhbGciOiJIUzM4NCJ9...")
        String token
) {}
