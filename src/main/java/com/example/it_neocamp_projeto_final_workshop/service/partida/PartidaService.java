package com.example.it_neocamp_projeto_final_workshop.service.partida;

import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPutRequest;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PartidaService {
    Partida cadastrarPartida(PartidaPostRequest partidaPostRequest) throws BadRequestException;
    Partida atualizarPartida(PartidaPutRequest partidaPutRequest);
    void deletarPartida(UUID partidaId);
    Partida listarPorId(UUID partidaId);
    Page<Partida> listarPartidas(String nomeClube, String nomeEstadio, Pageable pageable);
}
