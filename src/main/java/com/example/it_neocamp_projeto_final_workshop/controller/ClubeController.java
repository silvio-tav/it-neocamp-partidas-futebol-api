package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubes")
public class ClubeController {
    private final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarClube(
            @RequestBody @Valid ClubePostRequest clubePostRequest
            ){
        clubeService.cadastrarClube(clubePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
