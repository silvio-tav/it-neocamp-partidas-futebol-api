package com.example.it_neocamp_projeto_final_workshop;

import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.RetrospectoAdversarioIdView;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.RetrospectoView;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TestFixtures {
    public static final UUID CLUBE_CASA_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID CLUBE_VISITANTE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID ESTADIO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID PARTIDA_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    public static final LocalDate DATA_CRIACAO = LocalDate.of(1900, 1, 1);
    public static final LocalDateTime DATA_HORA_PARTIDA = LocalDateTime.of(2024, 6, 1, 16, 0);

    private TestFixtures() {
    }

    public static Clube clubeCasa() {
        return clube(CLUBE_CASA_ID, "Flamengo", EstadoBrasileiro.RJ, true);
    }

    public static Clube clubeVisitante() {
        return clube(CLUBE_VISITANTE_ID, "Palmeiras", EstadoBrasileiro.SP, true);
    }

    public static Clube clube(UUID id, String nome, EstadoBrasileiro estado, boolean ativo) {
        return Clube.builder()
                .clubeId(id)
                .nome(nome)
                .siglaEstado(estado)
                .dataCriacao(DATA_CRIACAO)
                .ativo(ativo)
                .build();
    }

    public static Estadio estadio() {
        return estadio(ESTADIO_ID, "Maracana");
    }

    public static Estadio estadio(UUID id, String nome) {
        return Estadio.builder()
                .estadioId(id)
                .nome(nome)
                .build();
    }

    public static Partida partida() {
        return partida(PARTIDA_ID, clubeCasa(), clubeVisitante(), estadio(), DATA_HORA_PARTIDA, 2, 1);
    }

    public static Partida partida(
            UUID id,
            Clube clubeCasa,
            Clube clubeVisitante,
            Estadio estadio,
            LocalDateTime dataHoraPartida,
            int golsCasa,
            int golsVisitante
    ) {
        return Partida.builder()
                .partidaId(id)
                .clubeCasa(clubeCasa)
                .clubeVisitante(clubeVisitante)
                .estadio(estadio)
                .dataHoraPartida(dataHoraPartida)
                .golsCasa(golsCasa)
                .golsVisitante(golsVisitante)
                .build();
    }

    public record RetrospectoStub(
            long totalJogos,
            long vitorias,
            long empates,
            long derrotas,
            long golsFeitos,
            long golsSofridos
    ) implements RetrospectoView {
        @Override
        public long getTotalJogos() {
            return totalJogos;
        }

        @Override
        public long getVitorias() {
            return vitorias;
        }

        @Override
        public long getEmpates() {
            return empates;
        }

        @Override
        public long getDerrotas() {
            return derrotas;
        }

        @Override
        public long getGolsFeitos() {
            return golsFeitos;
        }

        @Override
        public long getGolsSofridos() {
            return golsSofridos;
        }
    }

    public record RetrospectoAdversarioStub(
            UUID adversarioId,
            long totalJogos,
            long vitorias,
            long empates,
            long derrotas,
            long golsFeitos,
            long golsSofridos
    ) implements RetrospectoAdversarioIdView {
        @Override
        public UUID getAdversarioId() {
            return adversarioId;
        }

        @Override
        public long getTotalJogos() {
            return totalJogos;
        }

        @Override
        public long getVitorias() {
            return vitorias;
        }

        @Override
        public long getEmpates() {
            return empates;
        }

        @Override
        public long getDerrotas() {
            return derrotas;
        }

        @Override
        public long getGolsFeitos() {
            return golsFeitos;
        }

        @Override
        public long getGolsSofridos() {
            return golsSofridos;
        }
    }
}
