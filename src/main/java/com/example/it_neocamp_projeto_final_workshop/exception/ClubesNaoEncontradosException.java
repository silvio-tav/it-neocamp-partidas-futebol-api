package com.example.it_neocamp_projeto_final_workshop.exception;

import java.util.UUID;

public class ClubesNaoEncontradosException extends RuntimeException {
    public ClubesNaoEncontradosException(UUID clubeCasaId, UUID clubeVisitanteId) {
        super("Clubes com os ID´s "+clubeCasaId+" "+clubeVisitanteId+" não encontrados");
    }
}
