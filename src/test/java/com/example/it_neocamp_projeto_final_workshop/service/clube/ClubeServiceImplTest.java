package com.example.it_neocamp_projeto_final_workshop.service.clube;

import com.example.it_neocamp_projeto_final_workshop.TestFixtures;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePostRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.clube.ClubePutRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.ranking.RankingClubes;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.AdversarioPartidasRetrospecto;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.AdversarioRetrospecto;
import com.example.it_neocamp_projeto_final_workshop.dto.restrospecto.RetrospectoResponse;
import com.example.it_neocamp_projeto_final_workshop.enums.Atuacao;
import com.example.it_neocamp_projeto_final_workshop.enums.EstadoBrasileiro;
import com.example.it_neocamp_projeto_final_workshop.enums.RankingTipo;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.ClubeNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.model.Clube;
import com.example.it_neocamp_projeto_final_workshop.model.Partida;
import com.example.it_neocamp_projeto_final_workshop.repository.ClubeRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_CASA_ID;
import static com.example.it_neocamp_projeto_final_workshop.TestFixtures.CLUBE_VISITANTE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClubeServiceImplTest {
    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private ClubeServiceImpl clubeService;

    @Test
    void cadastrarClube_quandoNaoExiste_salvaClubeAtivo() {
        ClubePostRequest request = ClubePostRequest.builder()
                .nome("Flamengo")
                .estado(EstadoBrasileiro.RJ)
                .dataCriacao(LocalDate.of(1895, 11, 15))
                .build();
        when(clubeRepository.existsByNomeIgnoreCaseAndSiglaEstado("Flamengo", EstadoBrasileiro.RJ))
                .thenReturn(false);
        when(clubeRepository.save(any(Clube.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Clube clube = clubeService.cadastrarClube(request);

        assertThat(clube.getNome()).isEqualTo("Flamengo");
        assertThat(clube.getSiglaEstado()).isEqualTo(EstadoBrasileiro.RJ);
        assertThat(clube.getDataCriacao()).isEqualTo(LocalDate.of(1895, 11, 15));
        assertThat(clube.getAtivo()).isTrue();
    }

    @Test
    void cadastrarClube_quandoJaExiste_lancaExcecaoENaoSalva() {
        ClubePostRequest request = ClubePostRequest.builder()
                .nome("Flamengo")
                .estado(EstadoBrasileiro.RJ)
                .dataCriacao(LocalDate.of(1895, 11, 15))
                .build();
        when(clubeRepository.existsByNomeIgnoreCaseAndSiglaEstado("Flamengo", EstadoBrasileiro.RJ))
                .thenReturn(true);

        assertThatThrownBy(() -> clubeService.cadastrarClube(request))
                .isInstanceOf(ClubeJaExisteException.class)
                .hasMessageContaining("Flamengo")
                .hasMessageContaining("RJ");

        verify(clubeRepository, never()).save(any());
    }

    @Test
    void atualizarClube_quandoExiste_atualizaApenasCamposInformados() {
        Clube clube = TestFixtures.clubeCasa();
        ClubePutRequest request = ClubePutRequest.builder()
                .nome("Flamengo SAF")
                .build();
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(clube)).thenReturn(clube);

        Clube atualizado = clubeService.atualizarClube(request, CLUBE_CASA_ID);

        assertThat(atualizado.getNome()).isEqualTo("Flamengo SAF");
        assertThat(atualizado.getSiglaEstado()).isEqualTo(EstadoBrasileiro.RJ);
        assertThat(atualizado.getDataCriacao()).isEqualTo(TestFixtures.DATA_CRIACAO);
        assertThat(atualizado.getAtivo()).isTrue();
        verify(clubeRepository).save(clube);
    }

    @Test
    void atualizarClube_quandoNaoExiste_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubeService.atualizarClube(ClubePutRequest.builder().build(), CLUBE_CASA_ID))
                .isInstanceOf(ClubeNaoEncontradoException.class)
                .hasMessageContaining(CLUBE_CASA_ID.toString());

        verify(clubeRepository, never()).save(any());
    }

    @Test
    void inativarClube_quandoExiste_marcaComoInativo() {
        Clube clube = TestFixtures.clubeCasa();
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(clube));

        clubeService.inativarClube(CLUBE_CASA_ID);

        assertThat(clube.getAtivo()).isFalse();
        verify(clubeRepository).save(clube);
    }

    @Test
    void inativarClube_quandoNaoExiste_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubeService.inativarClube(CLUBE_CASA_ID))
                .isInstanceOf(ClubeNaoEncontradoException.class);

        verify(clubeRepository, never()).save(any());
    }

    @Test
    void listarClubePorId_quandoEncontrado_retornaClube() {
        Clube clube = TestFixtures.clubeCasa();
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.of(clube));

        Clube encontrado = clubeService.listarClubePorId(CLUBE_CASA_ID);

        assertThat(encontrado).isSameAs(clube);
    }

    @Test
    void listarClubePorId_quandoNaoEncontrado_lancaExcecao() {
        when(clubeRepository.findById(CLUBE_CASA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubeService.listarClubePorId(CLUBE_CASA_ID))
                .isInstanceOf(ClubeNaoEncontradoException.class);
    }

    @Test
    void listarClubes_quandoTemFiltros_delegaParaRepositorioComSpecification() {
        Pageable pageable = PageRequest.of(0, 10);
        Clube clube = TestFixtures.clubeCasa();
        Page<Clube> page = new PageImpl<>(List.of(clube));
        when(clubeRepository.findAll(anySpecification(), eq(pageable))).thenReturn(page);

        Page<Clube> resultado = clubeService.listarClubes("Fla", EstadoBrasileiro.RJ, true, pageable);

        assertThat(resultado.getContent()).containsExactly(clube);
        verify(clubeRepository).findAll(anySpecification(), eq(pageable));
    }

    @Test
    void retrospectoClube_quandoClubeNaoExiste_lancaExcecao() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(false);

        assertThatThrownBy(() -> clubeService.retrospectoClube(CLUBE_CASA_ID, null))
                .isInstanceOf(ClubeNaoEncontradoException.class);

        verifyNoInteractions(partidaRepository);
    }

    @Test
    void retrospectoClube_quandoNaoTemJogos_retornaZeros() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(partidaRepository.retrospecto(CLUBE_CASA_ID))
                .thenReturn(new TestFixtures.RetrospectoStub(0, 0, 0, 0, 0, 0));

        RetrospectoResponse response = clubeService.retrospectoClube(CLUBE_CASA_ID, null);

        assertThat(response.getTotalJogos()).isZero();
        assertThat(response.getVitorias()).isZero();
        assertThat(response.getEmpates()).isZero();
        assertThat(response.getDerrotas()).isZero();
        assertThat(response.getGolsFeitos()).isZero();
        assertThat(response.getGolsSofridos()).isZero();
    }

    @Test
    void retrospectoClube_quandoTemJogosMapeiaDadosDoRepositorio() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(partidaRepository.retrospectoMandante(CLUBE_CASA_ID))
                .thenReturn(new TestFixtures.RetrospectoStub(3, 2, 1, 0, 7, 3));

        RetrospectoResponse response = clubeService.retrospectoClube(CLUBE_CASA_ID, Atuacao.MANDANTE);

        assertThat(response.getTotalJogos()).isEqualTo(3);
        assertThat(response.getVitorias()).isEqualTo(2);
        assertThat(response.getEmpates()).isEqualTo(1);
        assertThat(response.getDerrotas()).isZero();
        assertThat(response.getGolsFeitos()).isEqualTo(7);
        assertThat(response.getGolsSofridos()).isEqualTo(3);
        verify(partidaRepository).retrospectoMandante(CLUBE_CASA_ID);
    }

    @Test
    void retrospectoClube_quandoAtuacaoVisitante_usaConsultaDeVisitante() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(partidaRepository.retrospectoVisitante(CLUBE_CASA_ID))
                .thenReturn(new TestFixtures.RetrospectoStub(1, 0, 0, 1, 1, 2));

        RetrospectoResponse response = clubeService.retrospectoClube(CLUBE_CASA_ID, Atuacao.VISITANTE);

        assertThat(response.getTotalJogos()).isEqualTo(1);
        assertThat(response.getDerrotas()).isEqualTo(1);
        verify(partidaRepository).retrospectoVisitante(CLUBE_CASA_ID);
    }

    @Test
    void retrospectoAdversarios_quandoNaoExistemPartidas_retornaListaVazia() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(partidaRepository.retrospectoPorAdversarioId(CLUBE_CASA_ID)).thenReturn(List.of());

        List<AdversarioRetrospecto> response = clubeService.retrospectoAdversarios(CLUBE_CASA_ID, null);

        assertThat(response).isEmpty();
        verify(clubeRepository, never()).findAllById(any());
    }

    @Test
    void retrospectoAdversarios_quandoExistemPartidas_mapeiaAdversarioEEstatisticas() {
        Clube adversario = TestFixtures.clubeVisitante();
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(partidaRepository.retrospectoPorAdversarioIdMandante(CLUBE_CASA_ID))
                .thenReturn(List.of(new TestFixtures.RetrospectoAdversarioStub(CLUBE_VISITANTE_ID, 2, 1, 1, 0, 4, 2)));
        when(clubeRepository.findAllById(List.of(CLUBE_VISITANTE_ID))).thenReturn(List.of(adversario));

        List<AdversarioRetrospecto> response = clubeService.retrospectoAdversarios(CLUBE_CASA_ID, Atuacao.MANDANTE);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getAdversario().getClubeId()).isEqualTo(CLUBE_VISITANTE_ID);
        assertThat(response.get(0).getTotalJogos()).isEqualTo(2);
        assertThat(response.get(0).getVitorias()).isEqualTo(1);
        assertThat(response.get(0).getEmpates()).isEqualTo(1);
        assertThat(response.get(0).getGolsFeitos()).isEqualTo(4);
        assertThat(response.get(0).getGolsSofridos()).isEqualTo(2);
    }

    @Test
    void retrospectoPartidasAdversario_quandoAdversarioNaoExiste_lancaExcecao() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(clubeRepository.existsById(CLUBE_VISITANTE_ID)).thenReturn(false);

        assertThatThrownBy(() -> clubeService.retrospectoPartidasAdversario(CLUBE_CASA_ID, CLUBE_VISITANTE_ID, null))
                .isInstanceOf(ClubeNaoEncontradoException.class)
                .hasMessageContaining(CLUBE_VISITANTE_ID.toString());
    }

    @Test
    void retrospectoPartidasAdversario_quandoNaoTemPartidas_retornaRetrospectoZerado() {
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(clubeRepository.existsById(CLUBE_VISITANTE_ID)).thenReturn(true);
        when(partidaRepository.findPartidasEntreClubes(CLUBE_CASA_ID, CLUBE_VISITANTE_ID)).thenReturn(List.of());

        AdversarioPartidasRetrospecto response =
                clubeService.retrospectoPartidasAdversario(CLUBE_CASA_ID, CLUBE_VISITANTE_ID, null);

        assertThat(response.getPartidas()).isEmpty();
        assertThat(response.getTotalJogos()).isZero();
        assertThat(response.getVitorias()).isZero();
        assertThat(response.getEmpates()).isZero();
        assertThat(response.getDerrotas()).isZero();
    }

    @Test
    void retrospectoPartidasAdversario_quandoTemPartidas_calculaResumoConsiderandoMandos() {
        Clube clube = TestFixtures.clubeCasa();
        Clube adversario = TestFixtures.clubeVisitante();
        Partida vitoriaComoCasa = TestFixtures.partida(UUID.randomUUID(), clube, adversario, TestFixtures.estadio(),
                TestFixtures.DATA_HORA_PARTIDA, 3, 1);
        Partida empateComoVisitante = TestFixtures.partida(UUID.randomUUID(), adversario, clube, TestFixtures.estadio(),
                TestFixtures.DATA_HORA_PARTIDA.plusDays(10), 2, 2);
        Partida derrotaComoVisitante = TestFixtures.partida(UUID.randomUUID(), adversario, clube, TestFixtures.estadio(),
                TestFixtures.DATA_HORA_PARTIDA.plusDays(20), 1, 0);
        when(clubeRepository.existsById(CLUBE_CASA_ID)).thenReturn(true);
        when(clubeRepository.existsById(CLUBE_VISITANTE_ID)).thenReturn(true);
        when(partidaRepository.findPartidasEntreClubesVisitante(CLUBE_CASA_ID, CLUBE_VISITANTE_ID))
                .thenReturn(List.of(empateComoVisitante, derrotaComoVisitante));

        AdversarioPartidasRetrospecto response =
                clubeService.retrospectoPartidasAdversario(CLUBE_CASA_ID, CLUBE_VISITANTE_ID, Atuacao.VISITANTE);

        assertThat(response.getPartidas()).hasSize(2);
        assertThat(response.getTotalJogos()).isEqualTo(2);
        assertThat(response.getVitorias()).isZero();
        assertThat(response.getEmpates()).isEqualTo(1);
        assertThat(response.getDerrotas()).isEqualTo(1);
        assertThat(response.getGolsFeitos()).isEqualTo(2);
        assertThat(response.getGolsSofridos()).isEqualTo(3);
    }

    @Test
    void ranking_porPontos_filtraSemPontosEOrdenaPorDesempates() {
        RankingClubes lider = new RankingClubes(UUID.randomUUID(), "A Clube", 10, 11, 3, 5);
        RankingClubes segundoPorVitorias = new RankingClubes(UUID.randomUUID(), "B Clube", 10, 8, 2, 5);
        RankingClubes semPontos = new RankingClubes(UUID.randomUUID(), "Z Clube", 0, 5, 0, 2);
        when(partidaRepository.calcularRankingBruto()).thenReturn(List.of(segundoPorVitorias, semPontos, lider));

        List<RankingClubes> ranking = clubeService.ranking(RankingTipo.PONTOS);

        assertThat(ranking).containsExactly(lider, segundoPorVitorias);
    }

    @Test
    void ranking_porGolsVitoriasEJogos_filtraZerosEOrdenaDecrescente() {
        RankingClubes maior = new RankingClubes(UUID.randomUUID(), "A Clube", 1, 9, 2, 4);
        RankingClubes menor = new RankingClubes(UUID.randomUUID(), "B Clube", 1, 3, 1, 2);
        RankingClubes zerado = new RankingClubes(UUID.randomUUID(), "C Clube", 0, 0, 0, 0);
        when(partidaRepository.calcularRankingBruto()).thenReturn(List.of(menor, zerado, maior));

        assertThat(clubeService.ranking(RankingTipo.GOLS)).containsExactly(maior, menor);
        assertThat(clubeService.ranking(RankingTipo.VITORIAS)).containsExactly(maior, menor);
        assertThat(clubeService.ranking(RankingTipo.JOGOS)).containsExactly(maior, menor);
    }

    @SuppressWarnings("unchecked")
    private static Specification<Clube> anySpecification() {
        return any(Specification.class);
    }
}
