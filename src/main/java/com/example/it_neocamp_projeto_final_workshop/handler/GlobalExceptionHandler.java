package com.example.it_neocamp_projeto_final_workshop.handler;

import com.example.it_neocamp_projeto_final_workshop.exception.*;
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

    @ExceptionHandler(EstadioJaExisteException.class)
    public ProblemDetail handleEstadioJaExiste(EstadioJaExisteException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EstadioNaoEncontradoException.class)
    public ProblemDetail handleEstadioNaoEncontrado(EstadioNaoEncontradoException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ClubesNaoEncontradosException.class)
    public ProblemDetail handleClubesNaoEncontrados(ClubesNaoEncontradosException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ClubesIguaisException.class)
    public ProblemDetail handleClubesIguais(ClubesIguaisException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ClubeInativoExcpetion.class)
    public ProblemDetail handleClubeInativo(ClubeInativoExcpetion ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ConflitoDeHorarioException.class)
    public ProblemDetail handleConflitoDeHorario(ConflitoDeHorarioException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EstadioOcupadoException.class)
    public ProblemDetail handleEstadioOcupado(EstadioOcupadoException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
}
