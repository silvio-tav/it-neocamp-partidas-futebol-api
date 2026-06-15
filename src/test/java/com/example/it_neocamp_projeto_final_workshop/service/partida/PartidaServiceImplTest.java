package com.example.it_neocamp_projeto_final_workshop.service.partida;

import com.example.it_neocamp_projeto_final_workshop.TestFixtures;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.partida.PartidaPutRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeInativoExcpetion;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubesIguaisException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubesNaoEncontradosException;
import com.example.it_neocamp_projeto_final_workshop.exception.ConflitoDeHorarioException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioOcupadoException;
import com.example.it_neocamp_projeto_final_workshop.exception.PartidaNaoEncontradaException;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
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

import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_CASA_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_VISITANTE_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.DATA_HORA_PARTIDA;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.ESTADIO_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.PARTIDA_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartidaServiceImplTest {
    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private PartidaServiceImpl partidaService;

    @Test
    void cadastrarPartida_quandoDadosValidos_salvaPartida() {
        Clube casa = TestFixtures.clubeCasa();
        Clube visitante = TestFixtures.clubeVisitante();
        Estadio estadio = TestFixtures.estadio();
        PartidaPostRequest request = postRequest();
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(casa));
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(ESTADIO_ID)).thenReturn(Optional.of(estadio));
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Partida partida = partidaService.cadastrarPartida(request);

        assertThat(partida.getClubeCasa()).isSameAs(casa);
        assertThat(partida.getClubeVisitante()).isSameAs(visitante);
        assertThat(partida.getEstadio()).isSameAs(estadio);
        assertThat(partida.getDataHoraPartida()).isEqualTo(DATA_HORA_PARTIDA);
        assertThat(partida.getGolsCasa()).isEqualTo(2);
        assertThat(partida.getGolsVisitante()).isEqualTo(1);
    }

    @Test
    void cadastrarPartida_quandoClubesIguais_lancaExcecaoAntesDeConsultarRepositorios() {
        PartidaPostRequest request = PartidaPostRequest.builder()
                .clubeCasaId(CLUBE_CASA_ID)
                .clubeVisitanteId(CLUBE_CASA_ID)
                .estadioId(ESTADIO_ID)
                .dataHoraPartida(DATA_HORA_PARTIDA)
                .golsCasa(1)
                .golsVisitante(1)
                .build();

        assertThatThrownBy(() -> partidaService.cadastrarPartida(request))
                .isInstanceOf(ClubesIguaisException.class)
                .hasMessageContaining("não podem ser iguais");

        verify(clubeRepository, never()).findById(any());
    }

    @Test
    void cadastrarPartida_quandoDoisClubesNaoExistem_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.empty());
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ClubesNaoEncontradosException.class)
                .hasMessageContaining(CLUBE_CASA_ID.toString())
                .hasMessageContaining(CLUBE_VISITANTE_ID.toString());
    }

    @Test
    void cadastrarPartida_quandoClubeCasaNaoExiste_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.empty());
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.of(TestFixtures.clubeVisitante()));

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ClubeNaoEncontradoException.class)
                .hasMessageContaining(CLUBE_CASA_ID.toString());
    }

    @Test
    void cadastrarPartida_quandoClubeVisitanteNaoExiste_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(TestFixtures.clubeCasa()));
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ClubeNaoEncontradoException.class)
                .hasMessageContaining(CLUBE_VISITANTE_ID.toString());
    }

    @Test
    void cadastrarPartida_quandoDoisClubesInativos_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID))
                .thenReturn(Optional.of(TestFixtures.clube(CLUBE_CASA_ID, "Casa", null, false)));
        when(clubeRepository.findById(CLUBE_VISITANTE_ID))
                .thenReturn(Optional.of(TestFixtures.clube(CLUBE_VISITANTE_ID, "Visitante", null, false)));

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ClubeInativoExcpetion.class)
                .hasMessageContaining("clubes informados estão inativos");
    }

    @Test
    void cadastrarPartida_quandoClubeCasaInativo_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID))
                .thenReturn(Optional.of(TestFixtures.clube(CLUBE_CASA_ID, "Casa", null, false)));
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.of(TestFixtures.clubeVisitante()));

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ClubeInativoExcpetion.class)
                .hasMessageContaining(CLUBE_CASA_ID.toString());
    }

    @Test
    void cadastrarPartida_quandoEstadioNaoExiste_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(TestFixtures.clubeCasa()));
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.of(TestFixtures.clubeVisitante()));
        when(estadioRepository.findById(ESTADIO_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(EstadioNaoEncontradoException.class)
                .hasMessageContaining(ESTADIO_ID.toString());
    }

    @Test
    void cadastrarPartida_quandoClubeCasaTemConflito_lancaExcecao() {
        preparaClubesEEstadioValidos();
        when(partidaRepository.existeConflitoDeHorario(
                eq(CLUBE_CASA_ID),
                eq(DATA_HORA_PARTIDA.minusHours(48)),
                eq(DATA_HORA_PARTIDA.plusHours(48)),
                eq((java.util.UUID) null)
        )).thenReturn(true);

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ConflitoDeHorarioException.class)
                .hasMessageContaining("Clube casa");

        verify(partidaRepository, never()).save(any());
    }

    @Test
    void cadastrarPartida_quandoClubeVisitanteTemConflito_lancaExcecao() {
        preparaClubesEEstadioValidos();
        when(partidaRepository.existeConflitoDeHorario(eq(CLUBE_CASA_ID), any(), any(), any())).thenReturn(false);
        when(partidaRepository.existeConflitoDeHorario(eq(CLUBE_VISITANTE_ID), any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(ConflitoDeHorarioException.class)
                .hasMessageContaining("Clube visitante");

        verify(partidaRepository, never()).save(any());
    }

    @Test
    void cadastrarPartida_quandoEstadioOcupadoNoDia_lancaExcecao() {
        preparaClubesEEstadioValidos();
        when(partidaRepository.existeConflitoDeHorario(any(), any(), any(), any())).thenReturn(false);
        when(partidaRepository.existePartidaNoEstadioNoDia(
                eq(ESTADIO_ID),
                eq(DATA_HORA_PARTIDA.toLocalDate().atStartOfDay()),
                eq(DATA_HORA_PARTIDA.toLocalDate().atStartOfDay().plusDays(1).minusNanos(1)),
                eq((java.util.UUID) null)
        )).thenReturn(true);

        assertThatThrownBy(() -> partidaService.cadastrarPartida(postRequest()))
                .isInstanceOf(EstadioOcupadoException.class)
                .hasMessageContaining(ESTADIO_ID.toString());

        verify(partidaRepository, never()).save(any());
    }

    @Test
    void atualizarPartida_quandoExiste_atualizaSomenteCamposInformados() {
        Partida existente = TestFixtures.partida();
        PartidaPutRequest request = PartidaPutRequest.builder()
                .golsCasa(5)
                .build();
        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.of(existente));
        preparaClubesEEstadioValidos();
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Partida atualizada = partidaService.atualizarPartida(PARTIDA_ID, request);

        assertThat(atualizada.getClubeCasa().getClubeId()).isEqualTo(CLUBE_CASA_ID);
        assertThat(atualizada.getClubeVisitante().getClubeId()).isEqualTo(CLUBE_VISITANTE_ID);
        assertThat(atualizada.getEstadio().getEstadioId()).isEqualTo(ESTADIO_ID);
        assertThat(atualizada.getDataHoraPartida()).isEqualTo(DATA_HORA_PARTIDA);
        assertThat(atualizada.getGolsCasa()).isEqualTo(5);
        assertThat(atualizada.getGolsVisitante()).isEqualTo(1);
    }

    @Test
    void atualizarPartida_quandoNaoExiste_lancaExcecao() {
        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidaService.atualizarPartida(PARTIDA_ID, PartidaPutRequest.builder().build()))
                .isInstanceOf(PartidaNaoEncontradaException.class)
                .hasMessageContaining(PARTIDA_ID.toString());
    }

    @Test
    void deletarPartida_quandoExiste_removePorId() {
        when(partidaRepository.existsById(PARTIDA_ID)).thenReturn(true);

        partidaService.deletarPartida(PARTIDA_ID);

        verify(partidaRepository).deleteById(PARTIDA_ID);
    }

    @Test
    void deletarPartida_quandoNaoExiste_lancaExcecao() {
        when(partidaRepository.existsById(PARTIDA_ID)).thenReturn(false);

        assertThatThrownBy(() -> partidaService.deletarPartida(PARTIDA_ID))
                .isInstanceOf(PartidaNaoEncontradaException.class);

        verify(partidaRepository, never()).deleteById(any());
    }

    @Test
    void listarPorId_quandoEncontrada_retornaPartida() {
        Partida partida = TestFixtures.partida();
        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.of(partida));

        Partida encontrada = partidaService.listarPorId(PARTIDA_ID);

        assertThat(encontrada).isSameAs(partida);
    }

    @Test
    void listarPorId_quandoNaoEncontrada_lancaExcecao() {
        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidaService.listarPorId(PARTIDA_ID))
                .isInstanceOf(PartidaNaoEncontradaException.class);
    }

    @Test
    void listarPartidas_comFiltros_delegaParaRepositorioComSpecification() {
        Pageable pageable = PageRequest.of(0, 10);
        Partida partida = TestFixtures.partida();
        Page<Partida> page = new PageImpl<>(List.of(partida));
        when(partidaRepository.findAll(anySpecification(), eq(pageable))).thenReturn(page);

        Page<Partida> resultado = partidaService.listarPartidas("Fla", "Mara", true, pageable);

        assertThat(resultado.getContent()).containsExactly(partida);
        verify(partidaRepository).findAll(anySpecification(), eq(pageable));
    }

    private void preparaClubesEEstadioValidos() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(TestFixtures.clubeCasa()));
        when(clubeRepository.findById(CLUBE_VISITANTE_ID)).thenReturn(Optional.of(TestFixtures.clubeVisitante()));
        when(estadioRepository.findById(ESTADIO_ID)).thenReturn(Optional.of(TestFixtures.estadio()));
    }

    private static PartidaPostRequest postRequest() {
        return PartidaPostRequest.builder()
                .clubeCasaId(CLUBE_CASA_ID)
                .clubeVisitanteId(CLUBE_VISITANTE_ID)
                .estadioId(ESTADIO_ID)
                .dataHoraPartida(DATA_HORA_PARTIDA)
                .golsCasa(2)
                .golsVisitante(1)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static Specification<Partida> anySpecification() {
        return any(Specification.class);
    }
}
