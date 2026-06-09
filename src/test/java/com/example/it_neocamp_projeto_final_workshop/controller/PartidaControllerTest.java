package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.TestFixtures;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPostRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubesIguaisException;
import com.example.it_neocamp_projeto_final_workshop.exception.ConflitoDeHorarioException;
import com.example.it_neocamp_projeto_final_workshop.exception.PartidaNaoEncontradaException;
import com.example.it_neocamp_projeto_final_workshop.handler.GlobalExceptionHandler;
import com.example.it_neocamp_projeto_final_workshop.service.partida.PartidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;

import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_CASA_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_VISITANTE_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.DATA_HORA_PARTIDA;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.ESTADIO_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.PARTIDA_ID;
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
class PartidaControllerTest {
    @Mock
    private PartidaService partidaService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new PartidaController(partidaService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void cadastrarPartida_quandoRequestValido_retornaCreated() throws Exception {
        PartidaPostRequest request = requestValido();
        when(partidaService.cadastrarPartida(any(PartidaPostRequest.class))).thenReturn(TestFixtures.partida());

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partidaId").value(PARTIDA_ID.toString()))
                .andExpect(jsonPath("$.clubeCasa.clubeId").value(CLUBE_CASA_ID.toString()))
                .andExpect(jsonPath("$.clubeVisitante.clubeId").value(CLUBE_VISITANTE_ID.toString()))
                .andExpect(jsonPath("$.estadio.estadioId").value(ESTADIO_ID.toString()))
                .andExpect(jsonPath("$.golsCasa").value(2))
                .andExpect(jsonPath("$.golsVisitante").value(1));
    }

    @Test
    void cadastrarPartida_quandoRequestInvalido_retornaBadRequestENaoChamaService() throws Exception {
        PartidaPostRequest request = PartidaPostRequest.builder()
                .clubeCasaId(null)
                .clubeVisitanteId(CLUBE_VISITANTE_ID)
                .estadioId(ESTADIO_ID)
                .dataHoraPartida(LocalDateTime.now().plusDays(1))
                .golsCasa(-1)
                .golsVisitante(1)
                .build();

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(partidaService, never()).cadastrarPartida(any());
    }

    @Test
    void cadastrarPartida_quandoClubesIguais_retornaBadRequest() throws Exception {
        when(partidaService.cadastrarPartida(any(PartidaPostRequest.class)))
                .thenThrow(new ClubesIguaisException("Clubes casa e visitante não podem ser iguais"));

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("não podem ser iguais")));
    }

    @Test
    void cadastrarPartida_quandoConflitoDeHorario_retornaConflict() throws Exception {
        when(partidaService.cadastrarPartida(any(PartidaPostRequest.class)))
                .thenThrow(new ConflitoDeHorarioException("conflito de horario"));

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("conflito")));
    }

    @Test
    void buscarPorId_quandoExiste_retornaOk() throws Exception {
        when(partidaService.listarPorId(PARTIDA_ID)).thenReturn(TestFixtures.partida());

        mockMvc.perform(get("/partidas/{partidaId}", PARTIDA_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidaId").value(PARTIDA_ID.toString()));
    }

    @Test
    void buscarPorId_quandoNaoExiste_retornaNotFound() throws Exception {
        when(partidaService.listarPorId(PARTIDA_ID)).thenThrow(new PartidaNaoEncontradaException(PARTIDA_ID));

        mockMvc.perform(get("/partidas/{partidaId}", PARTIDA_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString(PARTIDA_ID.toString())));
    }

    @Test
    void deletarPartida_retornaNoContent() throws Exception {
        mockMvc.perform(delete("/partidas/{partidaId}", PARTIDA_ID))
                .andExpect(status().isNoContent());

        verify(partidaService).deletarPartida(PARTIDA_ID);
    }

    private static PartidaPostRequest requestValido() {
        return PartidaPostRequest.builder()
                .clubeCasaId(CLUBE_CASA_ID)
                .clubeVisitanteId(CLUBE_VISITANTE_ID)
                .estadioId(ESTADIO_ID)
                .dataHoraPartida(DATA_HORA_PARTIDA)
                .golsCasa(2)
                .golsVisitante(1)
                .build();
    }
}
