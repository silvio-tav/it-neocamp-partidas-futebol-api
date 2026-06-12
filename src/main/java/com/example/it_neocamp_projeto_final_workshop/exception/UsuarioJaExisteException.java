package com.example.it_neocamp_projeto_final_workshop.exception;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException() {
        super("Username já cadastrado");
    }
}
