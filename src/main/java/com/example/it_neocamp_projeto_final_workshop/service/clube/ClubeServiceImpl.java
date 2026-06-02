package com.example.it_neocamp_projeto_final_workshop.service.clube;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.RetrospectoResponse;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.mapper.ClubeMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
import com.example.it_neocamp_projeto_final_workshop.repository.PartidaRepository;
import com.example.it_neocamp_projeto_final_workshop.specification.ClubeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClubeServiceImpl implements ClubeService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    public ClubeServiceImpl(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    @Override
    public Clube cadastrarClube(ClubePostRequest clubePostRequest) {
        if (clubeRepository.existsByNomeIgnoreCaseAndSiglaEstado(clubePostRequest.getNome(), clubePostRequest.getEstado())) {
            throw new ClubeJaExisteException(clubePostRequest.getNome(), clubePostRequest.getEstado().name());
        }
        Clube clube = ClubeMapper.toEntity(clubePostRequest);
        return clubeRepository.save(clube);
    }

    @Override
    public Clube atualizarClube(ClubePutRequest clubePutRequest, UUID clubeId) {
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

    @Override
    public void inativarClube(UUID clubeId) {
        Clube clube = clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ClubeNaoEncontradoException(clubeId));

        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    @Override
    public Clube listarClubePorId(UUID clubeId) {
        return clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ClubeNaoEncontradoException(clubeId));
    }

    @Override
    public Page<Clube> listarClubes(String nomeClube, EstadoBrasileiro siglaEstado, Boolean ativo, Pageable pageable) {
        Specification<Clube> spec = (root, query, cb) -> cb.conjunction();

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

    @Override
    public RetrospectoResponse retrospectoClube(UUID clubeId) {
        if (!clubeRepository.existsById(clubeId)) {
            throw new ClubeNaoEncontradoException(clubeId);
        }

        List<Partida> partidas = partidaRepository.findAllByClube(clubeId);
        if (partidas.isEmpty()) {
            return RetrospectoResponse.builder().build();
        }

        int vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;

        for (Partida partida : partidas) {
            boolean ehCasa = partida.getClubeCasa().getId().equals(clubeId);

            int golsDoClube      = ehCasa ? partida.getGolsCasa()      : partida.getGolsVisitante();
            int golsDoAdversario = ehCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            golsFeitos    += golsDoClube;
            golsSofridos  += golsDoAdversario;

            if (golsDoClube > golsDoAdversario) {
                vitorias++;
            } else if (golsDoClube == golsDoAdversario) {
                empates++;
            } else {
                derrotas++;
            }
        }

        return RetrospectoResponse.builder()
                .totalJogos(partidas.size())
                .vitorias(vitorias)
                .empates(empates)
                .derrotas(derrotas)
                .golsFeitos(golsFeitos)
                .golsSofridos(golsSofridos)
                .build();
    }
}
