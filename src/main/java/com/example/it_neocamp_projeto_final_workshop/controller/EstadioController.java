package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioResponse;
import com.example.it_neocamp_projeto_final_workshop.mapper.EstadioMapper;
import com.example.it_neocamp_projeto_final_workshop.service.estadio.EstadioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{estadioId}")
    public ResponseEntity<EstadioResponse> atualizarEstadio(
            @PathVariable
            Long estadioId,
            @RequestBody
            EstadioRequest estadioRequest
    ){
        return ResponseEntity.status(HttpStatus.OK).body(
                EstadioMapper.toResponse(estadioService.atualizarEstadio(estadioId, estadioRequest))
        );
    }

    @GetMapping
    public ResponseEntity<Page<EstadioResponse>> listarEstadios(
            @RequestParam
            String nomeEstadio,
            @PageableDefault Pageable pageable
            ){
        Page<EstadioResponse> page = estadioService.listarEstadios(nomeEstadio, pageable).map(EstadioMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{estadioId}")
    public ResponseEntity<EstadioResponse> listarPorId(
            @PathVariable
            Long estadioId
    ){
        return ResponseEntity.ok(EstadioMapper.toResponse(estadioService.listarEstadioPorId(estadioId)));
    }

    @DeleteMapping("/{estadioId}")
    public ResponseEntity<Void> deetarEstadio(
            @PathVariable
            Long estadioId
    ){
        estadioService.deletarEstadio(estadioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
