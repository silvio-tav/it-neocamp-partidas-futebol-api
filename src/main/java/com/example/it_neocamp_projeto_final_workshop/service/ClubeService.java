package com.example.it_neocamp_projeto_final_workshop.service;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.mapper.ClubeMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
import com.example.it_neocamp_projeto_final_workshop.specification.ClubeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Clube cadastrarClube(ClubePostRequest clubePostRequest) {
        if (clubeRepository.existsByNomeIgnoreCaseAndSiglaEstado(clubePostRequest.getNome(), clubePostRequest.getEstado())) {
            throw new ClubeJaExisteException(clubePostRequest.getNome(), clubePostRequest.getEstado().name());
        }
        Clube clube = ClubeMapper.toEntity(clubePostRequest);
        return clubeRepository.save(clube);
    }

    public Clube atualizarClube(ClubePutRequest clubePutRequest, Long clubeId){
        Clube clube = clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ClubeNaoEncontradoException(clubeId));

        if (clubePutRequest.getNome() != null) {
            clube.setNome(clubePutRequest.getNome());
        }
        if (clubePutRequest.getEstado() != null) {
            clube.setSiglaEstado(clubePutRequest.getEstado());
        }
        if (clubePutRequest.getDataCriacao() != null) {
            clube.setDataCriacao(clubePutRequest.getDataCriacao());
        }

        return clubeRepository.save(clube);
    }

    public void inativarClube(Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ClubeNaoEncontradoException(clubeId));

        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public Page<Clube> listarClubes(String nomeClube, EstadoBrasileiro siglaEstado, Boolean ativo, Pageable pageable) {
        Specification<Clube> spec = Specification.where((Specification<Clube>) null);

        if (nomeClube != null && !nomeClube.isBlank()) {
            spec = spec.and(ClubeSpecification.nomeContains(nomeClube));
        }
        if (siglaEstado != null) {
            spec = spec.and(ClubeSpecification.estadoEquals(siglaEstado));
        }
        if (ativo != null) {
            spec = spec.and(ClubeSpecification.ativoEquals(ativo));
        }

        return clubeRepository.findAll(spec, pageable);
    }
}
