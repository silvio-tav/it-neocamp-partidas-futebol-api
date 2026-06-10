package com.example.it_neocamp_projeto_final_workshop.specification;

import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PartidaSpecification {
    public static Specification<Partida> nomeClubeContains(String nomeClube){
        return (root, query, cb) -> {
            String pattern = "%" + nomeClube.toLowerCase() + "%";
            Predicate porClubeCasa = cb.like(cb.lower(root.join("clubeCasa").get("nome")), pattern);
            Predicate porClubeVisitante = cb.like(cb.lower(root.join("clubeVisitante").get("nome")), pattern);
            query.distinct(true);
            return cb.or(porClubeCasa, porClubeVisitante);
        };
    }

    public static Specification<Partida> nomeEstadioContains(String nomeEstadio) {
        return (root, query, cb) -> {
            String pattern = "%" + nomeEstadio.toLowerCase() + "%";
            return cb.like(cb.lower(root.join("estadio").get("nome")), pattern);
        };
    }

    public static Specification<Partida> apenasGoleadas() {
        return (root, query, cb) -> {
            jakarta.persistence.criteria.Expression<Integer> diff =
                cb.abs(cb.diff(root.get("golsCasa"), root.get("golsVisitante")));
            return cb.greaterThanOrEqualTo(diff, 3);
        };
    }
}
