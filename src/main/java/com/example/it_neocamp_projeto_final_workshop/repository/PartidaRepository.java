package com.example.it_neocamp_projeto_final_workshop.repository;

import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, UUID> {

    @Query("""
            SELECT COUNT(p) > 0 FROM Partida p
            WHERE (p.clubeCasa.id = :clubeId OR p.clubeVisitante.id = :clubeId)
            AND p.dataHoraPartida BETWEEN :inicio AND :fim
            """)
    boolean existeConflitoDeHorario(@Param("clubeId") UUID clubeId,
                                    @Param("inicio") LocalDateTime inicio,
                                    @Param("fim") LocalDateTime fim);

    @Query("""
            SELECT COUNT(p) > 0 FROM Partida p
            WHERE p.estadio.id = :estadioId
            AND p.dataHoraPartida BETWEEN :inicioDia AND :fimDia
            """)
    boolean existePartidaNoEstadioNoDia(@Param("estadioId") UUID estadioId,
                                        @Param("inicioDia") LocalDateTime inicioDia,
                                        @Param("fimDia") LocalDateTime fimDia);
}
