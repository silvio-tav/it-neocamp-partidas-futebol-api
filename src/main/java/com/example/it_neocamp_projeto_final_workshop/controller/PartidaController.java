package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPutRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaResponse;
import com.example.it_neocamp_projeto_final_workshop.mapper.PartidaMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import com.example.it_neocamp_projeto_final_workshop.service.partida.PartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/partidas")
@Tag(name = "Partidas", description = "Endpoints para gerenciamento de partidas de futebol")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar partida",
            description = "Cadastra uma nova partida. Valida clubes, estádio, conflito de horário e disponibilidade do estádio no dia."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partida cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou clubes iguais"),
            @ApiResponse(responseCode = "404", description = "Clube ou estádio não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário ou estádio já ocupado no dia")
    })
    public ResponseEntity<PartidaResponse> cadastrarPartida(
            @RequestBody @Valid PartidaPostRequest partidaPostRequest
    ) {
        Partida partida = partidaService.cadastrarPartida(partidaPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(PartidaMapper.toResponse(partida));
    }

    @PutMapping("/{partidaId}")
    @Operation(
            summary = "Atualizar partida",
            description = "Atualiza parcialmente os dados de uma partida. Apenas os campos enviados serão atualizados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou clubes iguais"),
            @ApiResponse(responseCode = "404", description = "Partida, clube ou estádio não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário ou estádio já ocupado no dia")
    })
    public ResponseEntity<PartidaResponse> atualizarPartida(
            @Parameter(description = "ID da partida a ser atualizada", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID partidaId,
            @RequestBody @Valid PartidaPutRequest partidaPutRequest
    ) {
        Partida partida = partidaService.atualizarPartida(partidaId, partidaPutRequest);
        return ResponseEntity.ok(PartidaMapper.toResponse(partida));
    }

    @DeleteMapping("/{partidaId}")
    @Operation(
            summary = "Deletar partida",
            description = "Remove uma partida pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Partida removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<Void> deletarPartida(
            @Parameter(description = "ID da partida a ser removida", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID partidaId
    ) {
        partidaService.deletarPartida(partidaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{partidaId}")
    @Operation(
            summary = "Buscar partida por ID",
            description = "Retorna os dados de uma partida específica pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<PartidaResponse> buscarPorId(
            @Parameter(description = "ID da partida a ser consultada", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID partidaId
    ) {
        return ResponseEntity.ok(PartidaMapper.toResponse(partidaService.listarPorId(partidaId)));
    }

    @GetMapping
    @Operation(
            summary = "Listar partidas",
            description = "Lista partidas com filtros opcionais por nome do clube (casa ou visitante) e nome do estádio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<PartidaResponse>> listarPartidas(
            @Parameter(description = "Filtrar por trecho do nome do clube (casa ou visitante)", example = "Fla")
            @RequestParam(required = false) String nomeClube,
            @Parameter(description = "Filtrar por trecho do nome do estádio", example = "Maracanã")
            @RequestParam(required = false) String nomeEstadio,
            @PageableDefault Pageable pageable
    ) {
        Page<PartidaResponse> page = partidaService.listarPartidas(nomeClube, nomeEstadio, pageable)
                .map(PartidaMapper::toResponse);
        return ResponseEntity.ok(page);
    }
}
