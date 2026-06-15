package com.example.it_neocamp_projeto_final_workshop.handler;

import com.example.it_neocamp_projeto_final_workshop.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(ClubeJaExisteException.class)
    public ProblemDetail handleClubeJaExiste(ClubeJaExisteException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ProblemDetail handleUsuarioJaExiste(UsuarioJaExisteException ex) {
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

    @ExceptionHandler(EstadioComPartidaException.class)
    public ProblemDetail handleEstadioComPartida(EstadioComPartidaException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(PartidaNaoEncontradaException.class)
    public ProblemDetail handlePartidaNaoEncontrada(PartidaNaoEncontradaException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
