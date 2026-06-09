package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.TestFixtures;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.handler.GlobalExceptionHandler;
import com.example.it_neocamp_projeto_final_workshop.service.estadio.EstadioService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.ESTADIO_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EstadioControllerTest {
    @Mock
    private EstadioService estadioService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new EstadioController(estadioService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void cadastrarEstadio_quandoRequestValido_retornaCreated() throws Exception {
        EstadioRequest request = EstadioRequest.builder().nome("Maracana").build();
        when(estadioService.cadastrarEsrtadio(any(EstadioRequest.class))).thenReturn(TestFixtures.estadio());

        mockMvc.perform(post("/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estadioId").value(ESTADIO_ID.toString()))
                .andExpect(jsonPath("$.nome").value("Maracana"));
    }

    @Test
    void cadastrarEstadio_quandoRequestInvalido_retornaBadRequestENaoChamaService() throws Exception {
        EstadioRequest request = EstadioRequest.builder().nome("AB").build();

        mockMvc.perform(post("/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(estadioService, never()).cadastrarEsrtadio(any());
    }

    @Test
    void cadastrarEstadio_quandoDuplicado_retornaConflict() throws Exception {
        EstadioRequest request = EstadioRequest.builder().nome("Maracana").build();
        when(estadioService.cadastrarEsrtadio(any(EstadioRequest.class)))
                .thenThrow(new EstadioJaExisteException("Maracana"));

        mockMvc.perform(post("/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("Maracana")));
    }

    @Test
    void atualizarEstadio_quandoValido_retornaOk() throws Exception {
        EstadioRequest request = EstadioRequest.builder().nome("Novo Maracana").build();
        when(estadioService.atualizarEstadio(any(), any(EstadioRequest.class)))
                .thenReturn(TestFixtures.estadio(ESTADIO_ID, "Novo Maracana"));

        mockMvc.perform(put("/estadios/{estadioId}", ESTADIO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Maracana"));
    }

    @Test
    void listarPorId_quandoNaoExiste_retornaNotFound() throws Exception {
        when(estadioService.listarEstadioPorId(ESTADIO_ID)).thenThrow(new EstadioNaoEncontradoException(ESTADIO_ID));

        mockMvc.perform(get("/estadios/{estadioId}", ESTADIO_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString(ESTADIO_ID.toString())));
    }

    @Test
    void deletarEstadio_retornaNoContent() throws Exception {
        mockMvc.perform(delete("/estadios/{estadioId}", ESTADIO_ID))
                .andExpect(status().isNoContent());

        verify(estadioService).deletarEstadio(ESTADIO_ID);
    }
}
