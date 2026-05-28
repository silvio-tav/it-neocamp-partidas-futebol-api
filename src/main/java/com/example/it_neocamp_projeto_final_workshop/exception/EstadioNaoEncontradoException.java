package com.example.it_neocamp_projeto_final_workshop.exception;

public class EstadioNaoEncontradoException extends RuntimeException {
    public EstadioNaoEncontradoException(Long estadioId) {
        super("Estadio com id "+" não encontrado");
    }
}
