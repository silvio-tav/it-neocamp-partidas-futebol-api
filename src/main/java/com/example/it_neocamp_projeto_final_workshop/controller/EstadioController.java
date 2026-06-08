package com.example.it_neocamp_projeto_final_workshop.controller;

import org.springframework.http.ProblemDetail;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioResponse;
import com.example.it_neocamp_projeto_final_workshop.mapper.EstadioMapper;
import com.example.it_neocamp_projeto_final_workshop.service.estadio.EstadioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.util.UUID;

@RestController
@RequestMapping("/estadios")
@Tag(name = "Estádios", description = "Endpoints para gerenciamento de estádios de futebol")
public class EstadioController {
    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar estádio", description = "Cadastra um novo estádio. Retorna 409 se já existir um estádio com o mesmo nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estádio cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Já existe um estádio com este nome",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
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
    @Operation(summary = "Atualizar estádio", description = "Atualiza o nome de um estádio existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estádio atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Já existe um estádio com este nome",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<EstadioResponse> atualizarEstadio(
            @Parameter(description = "ID do estádio a ser atualizado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID estadioId,
            @Valid @RequestBody EstadioRequest estadioRequest
    ){
        return ResponseEntity.status(HttpStatus.OK).body(
                EstadioMapper.toResponse(estadioService.atualizarEstadio(estadioId, estadioRequest))
        );
    }

    @GetMapping
    @Operation(summary = "Listar estádios", description = "Lista estádios com filtro opcional por nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<EstadioResponse>> listarEstadios(
            @Parameter(description = "Filtrar por trecho do nome do estádio", example = "Maracanã")
            @RequestParam(required = false) String nomeEstadio,
            @PageableDefault Pageable pageable
            ){
        Page<EstadioResponse> page = estadioService.listarEstadios(nomeEstadio, pageable).map(EstadioMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{estadioId}")
    @Operation(summary = "Buscar estádio por ID", description = "Retorna os dados de um estádio específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estádio encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<EstadioResponse> listarPorId(
            @Parameter(description = "ID do estádio a ser consultado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID estadioId
    ){
        return ResponseEntity.ok(EstadioMapper.toResponse(estadioService.listarEstadioPorId(estadioId)));
    }

    @DeleteMapping("/{estadioId}")
    @Operation(summary = "Deletar estádio", description = "Remove um estádio pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estádio removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> deletarEstadio(
            @Parameter(description = "ID do estádio a ser removido", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID estadioId
    ){
        estadioService.deletarEstadio(estadioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
