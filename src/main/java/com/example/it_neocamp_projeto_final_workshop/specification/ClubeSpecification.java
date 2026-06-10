package com.example.it_neocamp_projeto_final_workshop.specification;

import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import org.springframework.data.jpa.domain.Specification;

public class ClubeSpecification {

    public static Specification<Clube> nomeContains(String nome) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Clube> estadoEquals(EstadoBrasileiro estado) {
        return (root, query, cb) ->
                cb.equal(root.get("siglaEstado"), estado);
    }

    public static Specification<Clube> ativoEquals(Boolean ativo) {
        return (root, query, cb) ->
                cb.equal(root.get("ativo"), ativo);
    }
}
