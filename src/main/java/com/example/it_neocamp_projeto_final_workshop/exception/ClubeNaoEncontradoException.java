package com.example.it_neocamp_projeto_final_workshop.exception;

public class ClubeNaoEncontradoException extends RuntimeException {
    public ClubeNaoEncontradoException(Long id) {
        super("Clube com o id "+id.toString()+" não encontrado");
    }
}
