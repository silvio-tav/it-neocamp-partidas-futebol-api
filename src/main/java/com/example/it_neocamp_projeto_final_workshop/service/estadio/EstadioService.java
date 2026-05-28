package com.example.it_neocamp_projeto_final_workshop.service.estadio;

import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EstadioService {
    Estadio cadastrarEsrtadio(EstadioRequest estadioRequest);

    Estadio atualizarEstadio(UUID estadioId, EstadioRequest estadioRequest);

    Estadio listarEstadioPorId(UUID estadioId);

    Page<Estadio> listarEstadios(String nomdEstagio, Pageable pageable);

    void deletarEstadio(UUID estadioId);
}
