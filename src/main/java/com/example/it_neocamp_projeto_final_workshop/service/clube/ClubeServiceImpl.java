package com.example.it_neocamp_projeto_final_workshop.service.clube;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.ranking.RankingClubes;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.*;
import com.example.it_neocamp_projeto_final_workshop.enums.Atuacao;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.enums.RankingTipo;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.mapper.ClubeMapper;
import com.example.it_neocamp_projeto_final_workshop.mapper.PartidaMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
import com.example.it_neocamp_projeto_final_workshop.repository.PartidaRepository;
import com.example.it_neocamp_projeto_final_workshop.specification.ClubeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    public RetrospectoResponse retrospectoClube(UUID clubeId, Atuacao atuacao) {
        if (!clubeRepository.existsById(clubeId)) {
            throw new ClubeNaoEncontradoException(clubeId);
        }

        RetrospectoView r;
        if (atuacao == Atuacao.MANDANTE) {
            r = partidaRepository.retrospectoMandante(clubeId);
        } else if (atuacao == Atuacao.VISITANTE) {
            r = partidaRepository.retrospectoVisitante(clubeId);
        } else {
            r = partidaRepository.retrospecto(clubeId);
        }

        if (r.getTotalJogos() == 0) {
            return RetrospectoResponse.builder().build();
        }

        return RetrospectoResponse.builder()
                .totalJogos(Math.toIntExact(r.getTotalJogos()))
                .vitorias(Math.toIntExact(r.getVitorias()))
                .empates(Math.toIntExact(r.getEmpates()))
                .derrotas(Math.toIntExact(r.getDerrotas()))
                .golsFeitos(Math.toIntExact(r.getGolsFeitos()))
                .golsSofridos(Math.toIntExact(r.getGolsSofridos()))
                .build();
    }

    @Override
    public List<AdversarioRetrospecto> retrospectoAdversarios(UUID clubeId, Atuacao atuacao) {
        if (!clubeRepository.existsById(clubeId)) {
            throw new ClubeNaoEncontradoException(clubeId);
        }

        var rows = atuacao == Atuacao.MANDANTE
                ? partidaRepository.retrospectoPorAdversarioIdMandante(clubeId)
                : atuacao == Atuacao.VISITANTE
                    ? partidaRepository.retrospectoPorAdversarioIdVisitante(clubeId)
                    : partidaRepository.retrospectoPorAdversarioId(clubeId);
        if (rows.isEmpty()) return List.of();

        var ids = rows.stream().map(RetrospectoAdversarioIdView::getAdversarioId).toList();

        var clubes = clubeRepository.findAllById(ids);
        var clubePorId = clubes.stream()
                .collect(java.util.stream.Collectors.toMap(Clube::getClubeId, c -> c));

        return rows.stream()
                .map(r -> {
                    Clube adversario = clubePorId.get(r.getAdversarioId());
                    return AdversarioRetrospecto.builder()
                            .adversario(ClubeMapper.toResponse(adversario))
                            .totalJogos(Math.toIntExact(r.getTotalJogos()))
                            .vitorias(Math.toIntExact(r.getVitorias()))
                            .empates(Math.toIntExact(r.getEmpates()))
                            .derrotas(Math.toIntExact(r.getDerrotas()))
                            .golsFeitos(Math.toIntExact(r.getGolsFeitos()))
                            .golsSofridos(Math.toIntExact(r.getGolsSofridos()))
                            .build();
                })
                .toList();
    }

    @Override
    public AdversarioPartidasRetrospecto retrospectoPartidasAdversario(UUID clubeId, UUID adversarioId, Atuacao atuacao) {
        if (!clubeRepository.existsById(clubeId)) {
            throw new ClubeNaoEncontradoException(clubeId);
        }
        if (!clubeRepository.existsById(adversarioId)) {
            throw new ClubeNaoEncontradoException(adversarioId);
        }

        List<Partida> partidasEntreClubes = atuacao == Atuacao.MANDANTE
                ? partidaRepository.findPartidasEntreClubesMandante(clubeId, adversarioId)
                : atuacao == Atuacao.VISITANTE
                    ? partidaRepository.findPartidasEntreClubesVisitante(clubeId, adversarioId)
                    : partidaRepository.findPartidasEntreClubes(clubeId, adversarioId);

        int totalJogos = 0, vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;

        if (partidasEntreClubes.isEmpty()) {
            return AdversarioPartidasRetrospecto.builder()
                    .partidas(new ArrayList<>())
                    .empates(empates)
                    .vitorias(vitorias)
                    .golsSofridos(golsSofridos)
                    .totalJogos(totalJogos)
                    .golsFeitos(golsFeitos)
                    .derrotas(derrotas)
                    .build();
        }

        for (Partida partida: partidasEntreClubes) {
            boolean ehCasa = partida.getClubeCasa().getClubeId().equals(clubeId);
            int golsDoClube = ehCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsDoAdversario = ehCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            golsFeitos += golsDoClube;
            golsSofridos += golsDoAdversario;
            totalJogos++;
            if (golsDoClube > golsDoAdversario) {
                vitorias++;
            } else if (golsDoClube == golsDoAdversario) {
                empates++;
            } else {
                derrotas++;
            }
        }
        return AdversarioPartidasRetrospecto.builder()
                .partidas(
                        partidasEntreClubes.stream().map(PartidaMapper::toResponse).toList()
                )
                .empates(empates)
                .vitorias(vitorias)
                .golsSofridos(golsSofridos)
                .totalJogos(totalJogos)
                .golsFeitos(golsFeitos)
                .derrotas(derrotas)
                .build();
    }

    @Override
    public List<RankingClubes> ranking(RankingTipo rankingTipo) {
        List<RankingClubes> rankingClubes = partidaRepository.calcularRankingBruto();
        return switch (rankingTipo) {
            case PONTOS -> rankingClubes.stream()
                    .filter(r -> r.pontos() > 0)
                    .sorted(
                            Comparator
                                    .comparingLong(RankingClubes::pontos).reversed()
                                    .thenComparing(Comparator.comparingLong(RankingClubes::vitorias).reversed())
                                    .thenComparing(Comparator.comparingLong(RankingClubes::gols).reversed())
                                    .thenComparing(RankingClubes::nome)
                    ).toList();

            case GOLS -> rankingClubes.stream().filter(r -> r.gols() > 0)
                    .sorted(
                            Comparator
                                    .comparingLong(RankingClubes::gols).reversed()
                                    .thenComparing(RankingClubes::nome)
                    ).toList();

            case VITORIAS -> rankingClubes.stream().filter(r -> r.vitorias() > 0)
                    .sorted(
                            Comparator.comparingLong(RankingClubes::vitorias).reversed()
                                    .thenComparing(RankingClubes::nome)
                    ).toList();

            case JOGOS -> rankingClubes.stream().filter(r -> r.jogos() > 0)
                    .sorted(
                            Comparator.comparingLong(RankingClubes::jogos).reversed()
                                    .thenComparing(RankingClubes::nome)
                    )
                    .toList();
        };
    }
}
