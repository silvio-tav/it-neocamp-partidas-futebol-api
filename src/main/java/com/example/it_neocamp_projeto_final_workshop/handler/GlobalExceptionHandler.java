package com.example.it_neocamp_projeto_final_workshop.handler;

import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClubeJaExisteException.class)
    public ProblemDetail handleClubeJaExiste(ClubeJaExisteException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ClubeNaoEncontradoException.class)
    public ProblemDetail handleClubeNaoEncontrado(ClubeNaoEncontradoException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
