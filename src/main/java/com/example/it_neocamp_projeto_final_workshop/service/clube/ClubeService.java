package com.example.it_neocamp_projeto_final_workshop.service.clube;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ClubeService {

    Clube cadastrarClube(ClubePostRequest clubePostRequest);

    Clube atualizarClube(ClubePutRequest clubePutRequest, UUID clubeId);

    void inativarClube(UUID clubeId);

    Clube listarClubePorId(UUID clubeId);

    Page<Clube> listarClubes(String nomeClube, EstadoBrasileiro siglaEstado, Boolean ativo, Pageable pageable);
}
