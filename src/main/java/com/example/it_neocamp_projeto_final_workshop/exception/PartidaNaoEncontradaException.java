package com.example.it_neocamp_projeto_final_workshop.exception;

import java.util.UUID;

public class PartidaNaoEncontradaException extends RuntimeException {
    public PartidaNaoEncontradaException(UUID id) {
        super("Partida com o id " + id + " não encontrada");
    }
}
