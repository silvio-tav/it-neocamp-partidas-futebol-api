package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.*;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.mapper.ClubeMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.service.clube.ClubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            @Parameter(description = "ID do clube a ser atualizado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID clubeId,
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
    public ResponseEntity<Void> inativarClube(@PathVariable UUID clubeId) {
        clubeService.inativarClube(clubeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clubeId}")
    @Operation(
            summary = "Buscar clube por ID",
            description = "Retorna os dados de um clube específico pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clube encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado")
    })
    public ResponseEntity<ClubeResponse> buscarPorId(
            @Parameter(description = "ID do clube a ser consultado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID clubeId
    ){
        return ResponseEntity.ok(ClubeMapper.toResponse(clubeService.listarClubePorId(clubeId)));
    }

    @GetMapping
    @Operation(
            summary = "Listar clubes",
            description = "Lista clubes com filtros opcionais por nome, estado e situação. Os filtros podem ser combinados livremente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<ClubeResponse>> listarClubes(
            @Parameter(description = "Filtrar por trecho do nome do clube", example = "Fla")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Filtrar por estado", example = "RJ")
            @RequestParam(required = false) EstadoBrasileiro estado,
            @Parameter(description = "Filtrar por situação: true = ativos, false = inativos", example = "true")
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault Pageable pageable
    ) {
        Page<ClubeResponse> page = clubeService.listarClubes(nome, estado, ativo, pageable)
                .map(ClubeMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{clubeId}/retrospecto")
    @Operation(
            summary = "Retrospecto geral do clube",
            description = "Retorna o retrospecto geral de um clube: total de jogos, vitórias, empates, derrotas, gols feitos e gols sofridos em todas as partidas registradas. Caso o clube não possua nenhuma partida, todos os campos são retornados com valor zero."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrospecto retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado")
    })
    public ResponseEntity<RetrospectoResponse> retrospectoPorClube(
            @Parameter(description = "ID do clube cujo retrospecto será consultado", example = "b1b2c3d4-0001-0000-0000-000000000001")
            @PathVariable UUID clubeId
    ){
        return ResponseEntity.ok(clubeService.retrospectoClube(clubeId));
    }

    @GetMapping("/{clubeId}/retrospecto/adversarios")
    public ResponseEntity<List<RetrospectoAdversarioProjection>> retrospectoAdversarios(
            @PathVariable UUID clubeId
    ){
        return ResponseEntity.ok(clubeService.retrospectoAdversarios(clubeId));
    }
}
