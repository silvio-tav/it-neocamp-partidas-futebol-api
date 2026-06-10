package com.example.it_neocamp_projeto_final_workshop.exception;

import java.util.UUID;

public class EstadioOcupadoException extends RuntimeException {
    public EstadioOcupadoException(UUID estadioId) {
        super("Estádio com ID " + estadioId + " já possui partida registrada neste dia");
    }
}
