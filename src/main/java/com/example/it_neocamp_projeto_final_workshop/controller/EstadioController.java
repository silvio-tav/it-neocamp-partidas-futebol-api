package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioResponse;
import com.example.it_neocamp_projeto_final_workshop.mapper.EstadioMapper;
import com.example.it_neocamp_projeto_final_workshop.service.estadio.EstadioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadios")
public class EstadioController {
    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    public ResponseEntity<EstadioResponse> cadastrarEstadio(
            @Valid @RequestBody
            EstadioRequest estadioRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EstadioMapper.toResponse(
                        estadioService.cadastrarEsrtadio(estadioRequest))
                );
    }
}
