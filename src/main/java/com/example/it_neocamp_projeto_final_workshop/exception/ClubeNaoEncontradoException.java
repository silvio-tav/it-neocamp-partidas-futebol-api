package com.example.it_neocamp_projeto_final_workshop.exception;

import java.util.UUID;

public class ClubeNaoEncontradoException extends RuntimeException {
    public ClubeNaoEncontradoException(UUID id) {
        super("Clube com o id " + id + " não encontrado");
    }
}
