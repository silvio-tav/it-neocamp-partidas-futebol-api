package com.example.it_neocamp_projeto_final_workshop.exception;

public class ClubeJaExisteException extends RuntimeException {
    public ClubeJaExisteException(String nome, String estado) {
        super("Já existe um clube com o nome '" + nome + "' no estado " + estado);
    }
}
