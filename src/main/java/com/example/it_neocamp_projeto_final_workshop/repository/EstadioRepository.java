package com.example.it_neocamp_projeto_final_workshop.repository;

import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, UUID>, JpaSpecificationExecutor<Estadio> {
    boolean existsByNomeIgnoreCase(String nome);
}
