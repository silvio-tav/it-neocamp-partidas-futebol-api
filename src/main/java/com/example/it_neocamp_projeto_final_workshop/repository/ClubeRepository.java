package com.example.it_neocamp_projeto_final_workshop.repository;

import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, UUID>, JpaSpecificationExecutor<Clube> {
    boolean existsByNomeIgnoreCaseAndSiglaEstado(String nome, EstadoBrasileiro siglaEstado);
}
