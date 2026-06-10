package com.example.it_neocamp_projeto_final_workshop.specification;

import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import org.springframework.data.jpa.domain.Specification;

public class EstadioSpecification {
    public static Specification<Estadio> nomeContains(String nome){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }
}
