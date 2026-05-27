package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubeResponse;
import com.example.it_neocamp_projeto_final_workshop.mapper.ClubeMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.service.ClubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ClubeResponse> cadastrarClube(
            @RequestBody @Valid ClubePostRequest clubePostRequest
    ) {
        Clube clube = clubeService.cadastrarClube(clubePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClubeMapper.toResponse(clube));
    }

    @PutMapping("/{clubeId}")
    @Operation(
            summary = "Atualizar clube",
            description = "Atualiza parcialmente os dados de um clube existente. Apenas os campos enviados no body serão atualizados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clube atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (validação de campos)"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado")
    })
    public ResponseEntity<ClubeResponse> atualizarClube(
            @Parameter(description = "ID do clube a ser atualizado", example = "1")
            @PathVariable Long clubeId,
            @RequestBody ClubePutRequest clubePutRequest
            ){
        Clube clube = clubeService.atualizarClube(clubePutRequest, clubeId);
        return ResponseEntity.status(HttpStatus.OK).body(ClubeMapper.toResponse(clube));
    }

    @DeleteMapping("/{clubeId}")
    @Operation(
            summary = "Inativar clube",
            description = "Realiza a inativação (soft delete) de um clube pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Clube inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado")
    })
    public ResponseEntity<Void> inativarClube(@PathVariable Long clubeId) {
        clubeService.inativarClube(clubeId);
        return ResponseEntity.noContent().build();
    }
}
