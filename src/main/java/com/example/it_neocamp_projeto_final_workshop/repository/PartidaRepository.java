package com.example.it_neocamp_projeto_final_workshop.repository;

import com.example.it_neocamp_projeto_final_workshop.dto.ranking.RankingClubes;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.RetrospectoAdversarioIdView;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.RetrospectoView;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, UUID>, JpaSpecificationExecutor<Partida> {

    boolean existsByEstadioEstadioId(UUID estadioId);

    @Query("""
            SELECT COUNT(p) > 0 FROM Partida p
            WHERE (p.clubeCasa.clubeId = :clubeId OR p.clubeVisitante.clubeId = :clubeId)
            AND p.dataHoraPartida BETWEEN :inicio AND :fim
            """)
    boolean existeConflitoDeHorario(@Param("clubeId") UUID clubeId,
                                    @Param("inicio") LocalDateTime inicio,
                                    @Param("fim") LocalDateTime fim);

    @Query("""
            SELECT COUNT(p) > 0 FROM Partida p
            WHERE p.estadio.estadioId = :estadioId
            AND p.dataHoraPartida BETWEEN :inicioDia AND :fimDia
            """)
    boolean existePartidaNoEstadioNoDia(@Param("estadioId") UUID estadioId,
                                        @Param("inicioDia") LocalDateTime inicioDia,
                                        @Param("fimDia") LocalDateTime fimDia);

    @Query("""
  SELECT
    CASE
      WHEN p.clubeCasa.clubeId = :clubeId THEN p.clubeVisitante.clubeId
      ELSE p.clubeCasa.clubeId
    END as adversarioId,

    COUNT(p) as totalJogos,

    COALESCE(SUM(CASE
      WHEN (p.clubeCasa.clubeId = :clubeId AND p.golsCasa > p.golsVisitante)
        OR (p.clubeVisitante.clubeId = :clubeId AND p.golsVisitante > p.golsCasa)
      THEN 1 ELSE 0 END), 0) as vitorias,

    COALESCE(SUM(CASE WHEN p.golsCasa = p.golsVisitante THEN 1 ELSE 0 END), 0) as empates,

    COALESCE(SUM(CASE
      WHEN (p.clubeCasa.clubeId = :clubeId AND p.golsCasa < p.golsVisitante)
        OR (p.clubeVisitante.clubeId = :clubeId AND p.golsVisitante < p.golsCasa)
      THEN 1 ELSE 0 END), 0) as derrotas,

    COALESCE(SUM(CASE
      WHEN p.clubeCasa.clubeId = :clubeId THEN p.golsCasa
      ELSE p.golsVisitante END), 0) as golsFeitos,

    COALESCE(SUM(CASE
      WHEN p.clubeCasa.clubeId = :clubeId THEN p.golsVisitante
      ELSE p.golsCasa END), 0) as golsSofridos

  FROM Partida p
  WHERE p.clubeCasa.clubeId = :clubeId OR p.clubeVisitante.clubeId = :clubeId
  GROUP BY
    p.clubeCasa.clubeId, p.clubeVisitante.clubeId,
    CASE WHEN p.clubeCasa.clubeId = :clubeId THEN p.clubeVisitante.clubeId ELSE p.clubeCasa.clubeId END
""")
    List<RetrospectoAdversarioIdView> retrospectoPorAdversarioId(@Param("clubeId") UUID clubeId);

    @Query("""
  SELECT
    COUNT(p) as totalJogos,

    COALESCE(SUM(CASE
      WHEN (p.clubeCasa.clubeId = :clubeId AND p.golsCasa > p.golsVisitante)
        OR (p.clubeVisitante.clubeId = :clubeId AND p.golsVisitante > p.golsCasa)
      THEN 1 ELSE 0 END), 0) as vitorias,

    COALESCE(SUM(CASE WHEN p.golsCasa = p.golsVisitante THEN 1 ELSE 0 END), 0) as empates,

    COALESCE(SUM(CASE
      WHEN (p.clubeCasa.clubeId = :clubeId AND p.golsCasa < p.golsVisitante)
        OR (p.clubeVisitante.clubeId = :clubeId AND p.golsVisitante < p.golsCasa)
      THEN 1 ELSE 0 END), 0) as derrotas,

    COALESCE(SUM(CASE
      WHEN p.clubeCasa.clubeId = :clubeId THEN p.golsCasa
      ELSE p.golsVisitante END), 0) as golsFeitos,

    COALESCE(SUM(CASE
      WHEN p.clubeCasa.clubeId = :clubeId THEN p.golsVisitante
      ELSE p.golsCasa END), 0) as golsSofridos

  FROM Partida p
  WHERE p.clubeCasa.clubeId = :clubeId OR p.clubeVisitante.clubeId = :clubeId
""")
    RetrospectoView retrospecto(@Param("clubeId") UUID clubeId);

    @Query("""
   SELECT p
   FROM Partida p
   WHERE (p.clubeCasa.clubeId = :clubeId1 AND p.clubeVisitante.clubeId = :clubeId2)
      OR (p.clubeCasa.clubeId = :clubeId2 AND p.clubeVisitante.clubeId = :clubeId1)
""")
    List<Partida> findPartidasEntreClubes(@Param("clubeId1") UUID clubeId1,
                                          @Param("clubeId2") UUID clubeId2);

    @Query("""
   SELECT p FROM Partida p
   WHERE p.clubeCasa.clubeId = :clubeId AND p.clubeVisitante.clubeId = :adversarioId
""")
    List<Partida> findPartidasEntreClubesMandante(@Param("clubeId") UUID clubeId,
                                                   @Param("adversarioId") UUID adversarioId);

    @Query("""
   SELECT p FROM Partida p
   WHERE p.clubeVisitante.clubeId = :clubeId AND p.clubeCasa.clubeId = :adversarioId
""")
    List<Partida> findPartidasEntreClubesVisitante(@Param("clubeId") UUID clubeId,
                                                    @Param("adversarioId") UUID adversarioId);

    @Query("""
  SELECT
    COUNT(p) as totalJogos,
    COALESCE(SUM(CASE WHEN p.golsCasa > p.golsVisitante THEN 1 ELSE 0 END), 0) as vitorias,
    COALESCE(SUM(CASE WHEN p.golsCasa = p.golsVisitante THEN 1 ELSE 0 END), 0) as empates,
    COALESCE(SUM(CASE WHEN p.golsCasa < p.golsVisitante THEN 1 ELSE 0 END), 0) as derrotas,
    COALESCE(SUM(p.golsCasa), 0) as golsFeitos,
    COALESCE(SUM(p.golsVisitante), 0) as golsSofridos
  FROM Partida p WHERE p.clubeCasa.clubeId = :clubeId
""")
    RetrospectoView retrospectoMandante(@Param("clubeId") UUID clubeId);

    @Query("""
  SELECT
    COUNT(p) as totalJogos,
    COALESCE(SUM(CASE WHEN p.golsVisitante > p.golsCasa THEN 1 ELSE 0 END), 0) as vitorias,
    COALESCE(SUM(CASE WHEN p.golsCasa = p.golsVisitante THEN 1 ELSE 0 END), 0) as empates,
    COALESCE(SUM(CASE WHEN p.golsVisitante < p.golsCasa THEN 1 ELSE 0 END), 0) as derrotas,
    COALESCE(SUM(p.golsVisitante), 0) as golsFeitos,
    COALESCE(SUM(p.golsCasa), 0) as golsSofridos
  FROM Partida p WHERE p.clubeVisitante.clubeId = :clubeId
""")
    RetrospectoView retrospectoVisitante(@Param("clubeId") UUID clubeId);

    @Query("""
  SELECT
    p.clubeVisitante.clubeId as adversarioId,
    COUNT(p) as totalJogos,
    COALESCE(SUM(CASE WHEN p.golsCasa > p.golsVisitante THEN 1 ELSE 0 END), 0) as vitorias,
    COALESCE(SUM(CASE WHEN p.golsCasa = p.golsVisitante THEN 1 ELSE 0 END), 0) as empates,
    COALESCE(SUM(CASE WHEN p.golsCasa < p.golsVisitante THEN 1 ELSE 0 END), 0) as derrotas,
    COALESCE(SUM(p.golsCasa), 0) as golsFeitos,
    COALESCE(SUM(p.golsVisitante), 0) as golsSofridos
  FROM Partida p WHERE p.clubeCasa.clubeId = :clubeId
  GROUP BY p.clubeVisitante.clubeId
""")
    List<RetrospectoAdversarioIdView> retrospectoPorAdversarioIdMandante(@Param("clubeId") UUID clubeId);

    @Query("""
  SELECT
    p.clubeCasa.clubeId as adversarioId,
    COUNT(p) as totalJogos,
    COALESCE(SUM(CASE WHEN p.golsVisitante > p.golsCasa THEN 1 ELSE 0 END), 0) as vitorias,
    COALESCE(SUM(CASE WHEN p.golsCasa = p.golsVisitante THEN 1 ELSE 0 END), 0) as empates,
    COALESCE(SUM(CASE WHEN p.golsVisitante < p.golsCasa THEN 1 ELSE 0 END), 0) as derrotas,
    COALESCE(SUM(p.golsVisitante), 0) as golsFeitos,
    COALESCE(SUM(p.golsCasa), 0) as golsSofridos
  FROM Partida p WHERE p.clubeVisitante.clubeId = :clubeId
  GROUP BY p.clubeCasa.clubeId
""")
    List<RetrospectoAdversarioIdView> retrospectoPorAdversarioIdVisitante(@Param("clubeId") UUID clubeId);

    @Query("""
        select new com.example.it_neocamp_projeto_final_workshop.dto.ranking.RankingClubes(
            c.clubeId,
            c.nome,
            (
              coalesce(sum(case
                when p.clubeCasa = c and p.golsCasa > p.golsVisitante then 3
                when p.clubeVisitante = c and p.golsVisitante > p.golsCasa then 3
                when (p.clubeCasa = c or p.clubeVisitante = c) and p.golsCasa = p.golsVisitante then 1
                else 0
              end), 0)
            ),
            (
              coalesce(sum(case
                when p.clubeCasa = c then p.golsCasa
                when p.clubeVisitante = c then p.golsVisitante
                else 0
              end), 0)
            ),
            (
              coalesce(sum(case
                when p.clubeCasa = c and p.golsCasa > p.golsVisitante then 1
                when p.clubeVisitante = c and p.golsVisitante > p.golsCasa then 1
                else 0
              end), 0)
            ),
            (
              coalesce(sum(case
                when (p.clubeCasa = c or p.clubeVisitante = c) then 1
                else 0
              end), 0)
            )
        )
        from Clube c
        left join Partida p
          on p.clubeCasa = c or p.clubeVisitante = c
        where c.ativo = true
        group by c.clubeId, c.nome
    """)
    List<RankingClubes> calcularRankingBruto();
}