package com.example.it_neocamp_projeto_final_workshop.repository;

import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {
}
