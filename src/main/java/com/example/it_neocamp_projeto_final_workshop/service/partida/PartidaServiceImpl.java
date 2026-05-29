package com.example.it_neocamp_projeto_final_workshop.service.partida;

import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPutRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.*;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
import com.example.it_neocamp_projeto_final_workshop.repository.EstadioRepository;
import com.example.it_neocamp_projeto_final_workshop.repository.PartidaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PartidaServiceImpl implements PartidaService{
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;

    public PartidaServiceImpl(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }

    @Override
    public Partida cadastrarPartida(PartidaPostRequest partidaPostRequest) {
        ClubesValidados clubes = validarClubes(partidaPostRequest.getClubeCasaId(), partidaPostRequest.getClubeVisitanteId());
        Estadio estadio = validarEstadio(partidaPostRequest.getEstadioId());
        
        validarConflitoDeHorario(clubes.clubeCasa().getId(), clubes.clubeVisitante().getId(),
                partidaPostRequest.getDataHoraPartida());
        validarEstadioNoDia(estadio.getId(), partidaPostRequest.getDataHoraPartida());

        Partida partida = new Partida();
        partida.setClubeCasa(clubes.clubeCasa());
        partida.setClubeVisitante(clubes.clubeVisitante());
        partida.setEstadio(estadio);
        partida.setDataHoraPartida(partidaPostRequest.getDataHoraPartida());
        return partidaRepository.save(partida);
    }

    @Override
    public Partida atualizarPartida(PartidaPutRequest partidaPutRequest) {
        return null;
    }

    @Override
    public void deletarPartida(UUID partidaId) {

    }

    @Override
    public Partida listarPorId(UUID partidaId) {
        return null;
    }

    @Override
    public Page<Partida> listarPartidas(String nomeClube, String nomeEstadio, Pageable pageable) {
        return null;
    }

    private record ClubesValidados(Clube clubeCasa, Clube clubeVisitante) {}

    private ClubesValidados validarClubes(UUID clubeCasaId, UUID clubeVisitanteId) {
        if (clubeCasaId.equals(clubeVisitanteId)) {
            throw new ClubesIguaisException("Clubes casa e visitante não podem ser iguais");
        }
        Optional<Clube> clubeCasa = clubeRepository.findById(clubeCasaId);
        Optional<Clube> clubeVisitante = clubeRepository.findById(clubeVisitanteId);
        if (clubeCasa.isEmpty() && clubeVisitante.isEmpty()) {
            throw new ClubesNaoEncontradosException(clubeCasaId, clubeVisitanteId);
        } else if (clubeCasa.isEmpty()) {
            throw new ClubeNaoEncontradoException(clubeCasaId);
        } else if (clubeVisitante.isEmpty()) {
            throw new ClubeNaoEncontradoException(clubeVisitanteId);
        }

        if (!clubeCasa.get().getAtivo() && !clubeVisitante.get().getAtivo()) {
            throw new ClubeInativoExcpetion("Os clubes informados estão inativos");
        } else if (!clubeCasa.get().getAtivo()) {
            throw new ClubeInativoExcpetion("Clube com ID "+clubeCasaId+" está inativo");
        } else if (!clubeVisitante.get().getAtivo()) {
            throw new ClubeInativoExcpetion("Clube com ID "+clubeVisitanteId+" está inativo");
        }

        return new ClubesValidados(clubeCasa.get(), clubeVisitante.get());
    }

    private void validarConflitoDeHorario(UUID clubeCasaId, UUID clubeVisitanteId, LocalDateTime dataHora) {
        LocalDateTime inicio = dataHora.minusHours(48);
        LocalDateTime fim = dataHora.plusHours(48);
        if (partidaRepository.existeConflitoDeHorario(clubeCasaId, inicio, fim)) {
            throw new ConflitoDeHorarioException("Clube casa já possui partida em menos de 48 horas deste horário");
        }
        if (partidaRepository.existeConflitoDeHorario(clubeVisitanteId, inicio, fim)) {
            throw new ConflitoDeHorarioException("Clube visitante já possui partida em menos de 48 horas deste horário");
        }
    }

    private void validarEstadioNoDia(UUID estadioId, LocalDateTime dataHora) {
        LocalDateTime inicioDia = dataHora.toLocalDate().atStartOfDay();
        LocalDateTime fimDia = inicioDia.plusDays(1).minusNanos(1);
        if (partidaRepository.existePartidaNoEstadioNoDia(estadioId, inicioDia, fimDia)) {
            throw new EstadioOcupadoException(estadioId);
        }
    }

    private Estadio validarEstadio(UUID estadioId) {
        return estadioRepository.findById(estadioId)
                .orElseThrow(() -> new EstadioNaoEncontradoException(estadioId));
    }
}
