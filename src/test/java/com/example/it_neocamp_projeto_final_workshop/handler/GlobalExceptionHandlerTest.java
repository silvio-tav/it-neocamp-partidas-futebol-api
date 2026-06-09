package com.example.it_neocamp_projeto_final_workshop.handler;

import com.example.it_neocamp_projeto_final_workshop.exception.ClubeInativoExcpetion;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubesIguaisException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubesNaoEncontradosException;
import com.example.it_neocamp_projeto_final_workshop.exception.ConflitoDeHorarioException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioOcupadoException;
import com.example.it_neocamp_projeto_final_workshop.exception.PartidaNaoEncontradaException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleClubeJaExiste_retornaConflict() {
        ProblemDetail detail = handler.handleClubeJaExiste(new ClubeJaExisteException("Flamengo", "RJ"));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(detail.getDetail()).contains("Flamengo");
    }

    @Test
    void handleClubeNaoEncontrado_retornaNotFound() {
        UUID id = UUID.randomUUID();

        ProblemDetail detail = handler.handleClubeNaoEncontrado(new ClubeNaoEncontradoException(id));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getDetail()).contains(id.toString());
    }

    @Test
    void handleEstadioJaExiste_retornaConflict() {
        ProblemDetail detail = handler.handleEstadioJaExiste(new EstadioJaExisteException("Maracana"));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(detail.getDetail()).contains("Maracana");
    }

    @Test
    void handleEstadioNaoEncontrado_retornaNotFound() {
        UUID id = UUID.randomUUID();

        ProblemDetail detail = handler.handleEstadioNaoEncontrado(new EstadioNaoEncontradoException(id));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getDetail()).contains(id.toString());
    }

    @Test
    void handleClubesNaoEncontrados_retornaNotFound() {
        UUID casaId = UUID.randomUUID();
        UUID visitanteId = UUID.randomUUID();

        ProblemDetail detail = handler.handleClubesNaoEncontrados(new ClubesNaoEncontradosException(casaId, visitanteId));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getDetail()).contains(casaId.toString()).contains(visitanteId.toString());
    }

    @Test
    void handleClubesIguais_retornaBadRequest() {
        ProblemDetail detail = handler.handleClubesIguais(new ClubesIguaisException("clubes iguais"));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getDetail()).contains("clubes iguais");
    }

    @Test
    void handleClubeInativo_retornaConflict() {
        ProblemDetail detail = handler.handleClubeInativo(new ClubeInativoExcpetion("clube inativo"));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(detail.getDetail()).contains("clube inativo");
    }

    @Test
    void handleConflitoDeHorario_retornaConflict() {
        ProblemDetail detail = handler.handleConflitoDeHorario(new ConflitoDeHorarioException("conflito"));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(detail.getDetail()).contains("conflito");
    }

    @Test
    void handleEstadioOcupado_retornaConflict() {
        UUID id = UUID.randomUUID();

        ProblemDetail detail = handler.handleEstadioOcupado(new EstadioOcupadoException(id));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(detail.getDetail()).contains(id.toString());
    }

    @Test
    void handlePartidaNaoEncontrada_retornaNotFound() {
        UUID id = UUID.randomUUID();

        ProblemDetail detail = handler.handlePartidaNaoEncontrada(new PartidaNaoEncontradaException(id));

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getDetail()).contains(id.toString());
    }
}
