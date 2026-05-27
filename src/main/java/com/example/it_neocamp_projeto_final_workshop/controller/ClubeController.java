package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.service.ClubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubes")
@Tag(name = "Clubes", description = "Endpoints para gerenciamento de clubes de futebol brasileiros")
public class ClubeController {
    private final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar clube",
            description = "Cadastra um novo clube de futebol. Retorna 409 se já existir um clube com o mesmo nome no mesmo estado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clube cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (validação de campos)"),
            @ApiResponse(responseCode = "409", description = "Já existe um clube com o mesmo nome no estado informado")
    })
    public ResponseEntity<Void> cadastrarClube(
            @RequestBody @Valid ClubePostRequest clubePostRequest
    ) {
        clubeService.cadastrarClube(clubePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
