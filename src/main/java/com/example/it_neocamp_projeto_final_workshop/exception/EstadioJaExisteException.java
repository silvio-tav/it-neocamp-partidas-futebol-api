package com.example.it_neocamp_projeto_final_workshop.exception;

public class EstadioJaExisteException extends RuntimeException {
    public EstadioJaExisteException(String nome) {
        super("O estadio com o nome: "+nome+" já existe");
    }
}
