package com.example.it_neocamp_projeto_final_workshop.repository;

import com.example.it_neocamp_projeto_final_workshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}
