package com.example.it_neocamp_projeto_final_workshop.service.estadio;

import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstadioService {
    Estadio cadastrarEsrtadio(EstadioRequest estadioRequest);

    Estadio atualizarEstadio(Long estadioId, EstadioRequest estadioRequest);

    Estadio listarEstadioPorId(Long estadioId);

    Page<Estadio> listarEstadios(String nomdEstagio, Pageable pageable);

    void deletarEstadio(Long estadioId);
}
