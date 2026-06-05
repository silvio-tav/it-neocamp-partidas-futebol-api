package com.example.it_neocamp_projeto_final_workshop.dto.ranking;

import java.util.UUID;

public record RankingClubes(
        UUID clubeId,
        String nome,
        long pontos,
        long gols,
        long vitorias,
        long jogos
) { }
