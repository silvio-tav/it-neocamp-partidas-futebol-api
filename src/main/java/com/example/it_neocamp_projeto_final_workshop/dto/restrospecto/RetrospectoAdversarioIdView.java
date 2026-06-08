package com.example.it_neocamp_projeto_final_workshop.dto.restrospecto;

import java.util.UUID;

public interface RetrospectoAdversarioIdView {
    UUID getAdversarioId();
    long getTotalJogos();
    long getVitorias();
    long getEmpates();
    long getDerrotas();
    long getGolsFeitos();
    long getGolsSofridos();
}
