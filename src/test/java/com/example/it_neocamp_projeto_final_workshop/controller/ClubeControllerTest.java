package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.TestFixtures;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.ranking.RankingClubes;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.enums.RankingTipo;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.handler.GlobalExceptionHandler;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.service.clube.ClubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_CASA_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClubeControllerTest {
    @Mock
    private ClubeService clubeService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new ClubeController(clubeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void cadastrarClube_quandoRequestValido_retornaCreated() throws Exception {
        ClubePostRequest request = ClubePostRequest.builder()
                .nome("Flamengo")
                .estado(EstadoBrasileiro.RJ)
                .dataCriacao(LocalDate.of(1895, 11, 15))
                .build();
        Clube clube = TestFixtures.clube(CLUBE_CASA_ID, "Flamengo", EstadoBrasileiro.RJ, true);
        when(clubeService.cadastrarClube(any(ClubePostRequest.class))).thenReturn(clube);

        mockMvc.perform(post("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clubeId").value(CLUBE_CASA_ID.toString()))
                .andExpect(jsonPath("$.nome").value("Flamengo"))
                .andExpect(jsonPath("$.siglaEstado").value("RJ"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void cadastrarClube_quandoRequestInvalido_retornaBadRequestENaoChamaService() throws Exception {
        ClubePostRequest request = ClubePostRequest.builder()
                .nome("F")
                .estado(null)
                .dataCriacao(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(clubeService, never()).cadastrarClube(any());
    }

    @Test
    void cadastrarClube_quandoServiceLancaDuplicidade_retornaConflict() throws Exception {
        ClubePostRequest request = ClubePostRequest.builder()
                .nome("Flamengo")
                .estado(EstadoBrasileiro.RJ)
                .dataCriacao(LocalDate.of(1895, 11, 15))
                .build();
        when(clubeService.cadastrarClube(any(ClubePostRequest.class)))
                .thenThrow(new ClubeJaExisteException("Flamengo", "RJ"));

        mockMvc.perform(post("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("Flamengo")));
    }

    @Test
    void buscarPorId_quandoExiste_retornaOk() throws Exception {
        when(clubeService.listarClubePorId(CLUBE_CASA_ID)).thenReturn(TestFixtures.clubeCasa());

        mockMvc.perform(get("/clubes/{clubeId}", CLUBE_CASA_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubeId").value(CLUBE_CASA_ID.toString()));
    }

    @Test
    void buscarPorId_quandoNaoExiste_retornaNotFound() throws Exception {
        when(clubeService.listarClubePorId(CLUBE_CASA_ID)).thenThrow(new ClubeNaoEncontradoException(CLUBE_CASA_ID));

        mockMvc.perform(get("/clubes/{clubeId}", CLUBE_CASA_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString(CLUBE_CASA_ID.toString())));
    }

    @Test
    void inativarClube_retornaNoContent() throws Exception {
        mockMvc.perform(delete("/clubes/{clubeId}", CLUBE_CASA_ID))
                .andExpect(status().isNoContent());

        verify(clubeService).inativarClube(CLUBE_CASA_ID);
    }

    @Test
    void rankingClubes_retornaListaOrdenadaDoService() throws Exception {
        UUID clubeId = UUID.randomUUID();
        when(clubeService.ranking(RankingTipo.PONTOS))
                .thenReturn(List.of(new RankingClubes(clubeId, "Flamengo", 9, 8, 3, 4)));

        mockMvc.perform(get("/clubes/ranking").param("tipo", "PONTOS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clubeId").value(clubeId.toString()))
                .andExpect(jsonPath("$[0].pontos").value(9));

        verify(clubeService).ranking(RankingTipo.PONTOS);
    }
}
