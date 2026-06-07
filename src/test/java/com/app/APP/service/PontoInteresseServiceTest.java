package com.app.APP.service;

import com.app.APP.entity.Caminho;
import com.app.APP.entity.Evidencia;
import com.app.APP.entity.PontoInteresse;
import com.app.APP.model.dto.request.EvidenciaRequest;
import com.app.APP.model.dto.request.PontoInteresseRequest;
import com.app.APP.model.dto.response.EvidenciaResponse;
import com.app.APP.model.dto.response.PontoInteresseResponse;
import com.app.APP.repository.CaminhoRepository;
import com.app.APP.repository.EvidenciaRepository;
import com.app.APP.repository.PontoInteresseRepository;
import com.app.APP.stub.CaminhoStub;
import com.app.APP.stub.PontoInteresseStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PontoInteresseService")
class PontoInteresseServiceTest {

    @Mock
    private PontoInteresseRepository pontoRepository;
    @Mock
    private EvidenciaRepository evidenciaRepository;
    @Mock
    private CaminhoRepository caminhoRepository;

    @InjectMocks
    private PontoInteresseService service;

    @Test
    @DisplayName("create deve persistir ponto quando caminho existe")
    void deveCriarPonto() {
        PontoInteresseRequest request = PontoInteresseStub.umRequest();
        Caminho caminho = CaminhoStub.umCaminho().build();
        when(caminhoRepository.findById(request.caminhoId())).thenReturn(Optional.of(caminho));
        when(pontoRepository.save(any(PontoInteresse.class))).thenAnswer(inv -> inv.getArgument(0));

        PontoInteresseResponse response = service.create(request);

        assertThat(response.nome()).isEqualTo(request.nome());
        assertThat(response.tipo()).isEqualTo(request.tipo());
        verify(pontoRepository).save(any(PontoInteresse.class));
    }

    @Test
    @DisplayName("create deve falhar quando caminho nao existe")
    void deveFalharCriarSemCaminho() {
        PontoInteresseRequest request = PontoInteresseStub.umRequest();
        when(caminhoRepository.findById(request.caminhoId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Caminho nao encontrado");

        verify(pontoRepository, never()).save(any());
    }

    @Test
    @DisplayName("adicionarEvidencia deve aceitar captura dentro do raio de 50m")
    void deveAceitarEvidenciaProxima() {
        PontoInteresse ponto = PontoInteresseStub.umPonto().build();
        EvidenciaRequest request = PontoInteresseStub.umRequestEvidenciaProxima();
        when(pontoRepository.findById(request.pontoId())).thenReturn(Optional.of(ponto));
        when(evidenciaRepository.save(any(Evidencia.class))).thenAnswer(inv -> inv.getArgument(0));

        EvidenciaResponse response = service.adicionarEvidencia(request);

        assertThat(response.validada()).isTrue();
        assertThat(response.pontoId()).isEqualTo(ponto.getId());
        verify(evidenciaRepository).save(any(Evidencia.class));
    }

    @Test
    @DisplayName("adicionarEvidencia deve rejeitar captura alem de 50m")
    void deveRejeitarEvidenciaLonge() {
        PontoInteresse ponto = PontoInteresseStub.umPonto().build();
        EvidenciaRequest request = PontoInteresseStub.umRequestEvidenciaLonge();
        when(pontoRepository.findById(request.pontoId())).thenReturn(Optional.of(ponto));

        assertThatThrownBy(() -> service.adicionarEvidencia(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("muito longe");

        verify(evidenciaRepository, never()).save(any());
    }

    @Test
    @DisplayName("adicionarEvidencia deve falhar quando ponto nao existe")
    void deveFalharEvidenciaSemPonto() {
        EvidenciaRequest request = PontoInteresseStub.umRequestEvidenciaProxima();
        when(pontoRepository.findById(request.pontoId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionarEvidencia(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ponto nao encontrado");
    }

    @Test
    @DisplayName("getById deve calcular nivel maximo (3+ usuarios e descricao)")
    void deveCalcularNivelMaximo() {
        PontoInteresse ponto = PontoInteresseStub.umPonto().build();
        when(pontoRepository.findById(ponto.getId())).thenReturn(Optional.of(ponto));
        when(evidenciaRepository.countUsuariosValidadosByPontoId(ponto.getId())).thenReturn(3L);

        PontoInteresseResponse response = service.getById(ponto.getId());

        assertThat(response.nivelConfianca()).isEqualTo(5);
    }

    @Test
    @DisplayName("getById deve calcular nivel minimo (sem evidencia, sem descricao)")
    void deveCalcularNivelMinimo() {
        PontoInteresse ponto = PontoInteresseStub.umPonto().descricao(null).build();
        when(pontoRepository.findById(ponto.getId())).thenReturn(Optional.of(ponto));
        when(evidenciaRepository.countUsuariosValidadosByPontoId(ponto.getId())).thenReturn(0L);

        PontoInteresseResponse response = service.getById(ponto.getId());

        assertThat(response.nivelConfianca()).isEqualTo(1);
    }

    @Test
    @DisplayName("getByCaminho deve paginar, mapear e calcular nivel via contagem em lote")
    void deveListarPorCaminho() {
        Pageable pageable = PageRequest.of(0, 10);
        PontoInteresse ponto = PontoInteresseStub.umPonto().build();
        when(pontoRepository.findByCaminhoId(CaminhoStub.ID, pageable))
                .thenReturn(new PageImpl<>(List.of(ponto)