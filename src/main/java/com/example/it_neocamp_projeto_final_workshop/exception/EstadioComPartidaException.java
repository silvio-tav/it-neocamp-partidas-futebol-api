package com.example.it_neocamp_projeto_final_workshop.exception;

import java.util.UUID;

public class EstadioComPartidaException extends RuntimeException {
    public EstadioComPartidaException(UUID estadioId) {
        super("Estádio com ID " + estadioId + " possui partidas cadastradas e não pode ser excluído");
    }
}
