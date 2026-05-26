package com.example.it_neocamp_projeto_final_workshop.service;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.mapper.ClubeMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
import org.springframework.stereotype.Service;

@Service
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Clube cadastrarClube(ClubePostRequest clubePostRequest) {
        Clube clube = ClubeMapper.toEntity(clubePostRequest);
        return clubeRepository.save(clube);
    }
}
