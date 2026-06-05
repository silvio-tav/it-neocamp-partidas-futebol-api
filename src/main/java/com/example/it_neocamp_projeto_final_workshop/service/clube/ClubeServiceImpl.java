package com.example.it_neocamp_projeto_final_workshop.service.clube;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.ranking.RankingClubes;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.AdversarioPartidasRetrospecto;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.AdversarioRetrospecto;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.RetrospectoResponse;
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
            boolean ehCasa = partida.getClubeCasa().getClubeId().equals(clubeId);

            int golsDoClube = ehCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsDoAdversario = ehCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            golsFeitos += golsDoClube;
            golsSofridos += golsDoAdversario;

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

    @Override
    public List<AdversarioRetrospecto> retrospectoAdversarios(UUID clubeId) {
        if (!clubeRepository.existsById(clubeId)) {
            throw new ClubeNaoEncontradoException(clubeId);
        }
        List<Partida> partidas = partidaRepository.findAllByClube(clubeId);

        List<AdversarioRetrospecto> adversarioRetrospectos = new ArrayList<>();
        if (partidas.isEmpty()) {
            return adversarioRetrospectos;
        }

        for (Partida partida : partidas) {
            boolean ehClubeCasa = partida.getClubeCasa().getClubeId().equals(clubeId);
            Clube clubeAdversario = ehClubeCasa ? partida.getClubeVisitante() : partida.getClubeCasa();
            boolean jaAdicionado = false;

            for (AdversarioRetrospecto adversarioRetrospecto : adversarioRetrospectos) {
                if (adversarioRetrospecto.getAdversario().getClubeId().equals(clubeAdversario.getClubeId())) {
                    jaAdicionado = true;
                    break;
                }
            }

            if (!jaAdicionado) {
                int totalJogos = 0, vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;
                for (Partida partidasDosClubes : partidas) {
                    if (
                            partidasDosClubes.getClubeCasa().getClubeId().equals(clubeAdversario.getClubeId()) ||
                                    partidasDosClubes.getClubeVisitante().getClubeId().equals(clubeAdversario.getClubeId())
                    ) {
                        boolean ehCasaNaPartidaInterna = partidasDosClubes.getClubeCasa().getClubeId().equals(clubeId);
                        int golsDoClube      = ehCasaNaPartidaInterna ? partidasDosClubes.getGolsCasa()      : partidasDosClubes.getGolsVisitante();
                        int golsDoAdversario = ehCasaNaPartidaInterna ? partidasDosClubes.getGolsVisitante() : partidasDosClubes.getGolsCasa();

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
                }
                adversarioRetrospectos.add(AdversarioRetrospecto.builder()
                                .adversario(ClubeMapper.toResponse(clubeAdversario))
                                .empates(empates)
                                .vitorias(vitorias)
                                .derrotas(derrotas)
                                .golsFeitos(golsFeitos)
                                .totalJogos(totalJogos)
                                .golsSofridos(golsSofridos)
                        .build());
            }
        }
        return adversarioRetrospectos;
    }

    @Override
    public AdversarioPartidasRetrospecto retrospectoPartidasAdversario(UUID clubeId, UUID adversarioId) {
        if (!clubeRepository.existsById(clubeId)) {
            throw new ClubeNaoEncontradoException(clubeId);
        }
        if (!clubeRepository.existsById(adversarioId)) {
            throw new ClubeNaoEncontradoException(adversarioId);
        }
        List<Partida> partidasEntreClubes = partidaRepository.findPartidasEntreClubes(clubeId, adversarioId);

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
                                    .thenComparingLong(RankingClubes::vitorias).reversed()
                                    .thenComparingLong(RankingClubes::gols).reversed()
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