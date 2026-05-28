package com.example.it_neocamp_projeto_final_workshop.exception;

import java.util.UUID;

public class EstadioNaoEncontradoException extends RuntimeException {
    public EstadioNaoEncontradoException(UUID estadioId) {
        super("Estadio com id " + estadioId + " não encontrado");
    }
}
