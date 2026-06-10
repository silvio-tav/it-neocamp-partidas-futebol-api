package com.example.it_neocamp_projeto_final_workshop.service.estadio;

import com.example.it_neocamp_projeto_final_workshop.TestFixtures;
import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioComPartidaException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import com.example.it_neocamp_projeto_final_workshop.repository.EstadioRepository;
import com.example.it_neocamp_projeto_final_workshop.repository.PartidaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.ESTADIO_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadioServiceImplTest {
    @Mock
    private EstadioRepository estadioRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private EstadioServiceImpl estadioService;

    @Test
    void cadastrarEstadio_quandoNaoExiste_salvaEstadio() {
        EstadioRequest request = EstadioRequest.builder().nome("Maracana").build();
        when(estadioRepository.existsByNomeIgnoreCase("Maracana")).thenReturn(false);
        when(estadioRepository.save(any(Estadio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Estadio estadio = estadioService.cadastrarEsrtadio(request);

        assertThat(estadio.getNome()).isEqualTo("Maracana");
    }

    @Test
    void cadastrarEstadio_quandoJaExiste_lancaExcecaoENaoSalva() {
        EstadioRequest request = EstadioRequest.builder().nome("Maracana").build();
        when(estadioRepository.existsByNomeIgnoreCase("Maracana")).thenReturn(true);

        assertThatThrownBy(() -> estadioService.cadastrarEsrtadio(request))
                .isInstanceOf(EstadioJaExisteException.class)
                .hasMessageContaining("Maracana");

        verify(estadioRepository, never()).save(any());
    }

    @Test
    void atualizarEstadio_quandoExiste_atualizaNomePreservandoId() {
        Estadio existente = TestFixtures.estadio();
        EstadioRequest request = EstadioRequest.builder().nome("Novo Maracana").build();
        when(estadioRepository.findById(ESTADIO_ID)).thenReturn(Optional.of(existente));
        when(estadioRepository.save(any(Estadio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Estadio atualizado = estadioService.atualizarEstadio(ESTADIO_ID, request);

        assertThat(atualizado.getEstadioId()).isEqualTo(ESTADIO_ID);
        assertThat(atualizado.getNome()).isEqualTo("Novo Maracana");
        verify(estadioRepository).save(existente);
    }

    @Test
    void atualizarEstadio_quandoNaoExiste_lancaExcecao() {
        when(estadioRepository.findById(ESTADIO_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> estadioService.atualizarEstadio(ESTADIO_ID, EstadioRequest.builder().nome("Arena").build()))
                .isInstanceOf(EstadioNaoEncontradoException.class)
                .hasMessageContaining(ESTADIO_ID.toString());

        verify(estadioRepository, never()).save(any());
    }

    @Test
    void listarEstadioPorId_quandoEncontrado_retornaEstadio() {
        Estadio estadio = TestFixtures.estadio();
        when(estadioRepository.findById(ESTADIO_ID)).thenReturn(Optional.of(estadio));

        Estadio encontrado = estadioService.listarEstadioPorId(ESTADIO_ID);

        assertThat(encontrado).isSameAs(estadio);
    }

    @Test
    void listarEstadios_comFiltro_delegaParaRepositorioComSpecification() {
        Pageable pageable = PageRequest.of(0, 10);
        Estadio estadio = TestFixtures.estadio();
        Page<Estadio> page = new PageImpl<>(List.of(estadio));
        when(estadioRepository.findAll(anySpecification(), eq(pageable))).thenReturn(page);

        Page<Estadio> resultado = estadioService.listarEstadios("Mara", pageable);

        assertThat(resultado.getContent()).containsExactly(estadio);
        verify(estadioRepository).findAll(anySpecification(), eq(pageable));
    }

    @Test
    void deletarEstadio_quandoExiste_removePorId() {
        when(estadioRepository.existsById(ESTADIO_ID)).thenReturn(true);
        when(partidaRepository.existsByEstadioEstadioId(ESTADIO_ID)).thenReturn(false);

        estadioService.deletarEstadio(ESTADIO_ID);

        verify(estadioRepository).deleteById(ESTADIO_ID);
    }

    @Test
    void deletarEstadio_quandoNaoExiste_lancaExcecao() {
        when(estadioRepository.existsById(ESTADIO_ID)).thenReturn(false);

        assertThatThrownBy(() -> estadioService.deletarEstadio(ESTADIO_ID))
                .isInstanceOf(EstadioNaoEncontradoException.class);

        verify(estadioRepository, never()).deleteById(ESTADIO_ID);
    }

    @Test
    void deletarEstadio_quandoPossuiPartidas_lancaExcecao() {
        when(estadioRepository.existsById(ESTADIO_ID)).thenReturn(true);
        when(partidaRepository.existsByEstadioEstadioId(ESTADIO_ID)).thenReturn(true);

        assertThatThrownBy(() -> estadioService.deletarEstadio(ESTADIO_ID))
                .isInstanceOf(EstadioComPartidaException.class)
                .hasMessageContaining(ESTADIO_ID.toString());

        verify(estadioRepository, never()).deleteById(ESTADIO_ID);
    }

    @SuppressWarnings("unchecked")
    private static Specification<Estadio> anySpecification() {
        return any(Specification.class);
    }
}
