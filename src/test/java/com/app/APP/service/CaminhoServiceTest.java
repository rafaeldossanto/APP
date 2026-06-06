package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.dto.response.CaminhoResponse;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.CaminhoRepository;
import com.app.APP.stub.AventuraStub;
import com.app.APP.stub.CaminhoStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CaminhoService")
class CaminhoServiceTest {

    @Mock
    private CaminhoRepository caminhoRepository;
    @Mock
    private AventuraRepository aventuraRepository;

    @InjectMocks
    private CaminhoService service;

    @Test
    @DisplayName("iniciar deve persistir caminho quando aventura existe")
    void deveIniciarCaminho() {
        CaminhoRequest request = CaminhoStub.umRequest();
        Aventura aventura = AventuraStub.umaAventura().build();
        Caminho salvo = CaminhoStub.umCaminho().build();

        when(aventuraRepository.findById(request.aventuraId())).thenReturn(Optional.of(aventura));
        when(caminhoRepository.save(any(Caminho.class))).thenReturn(salvo);

        CaminhoResponse response = service.iniciar(request);

        assertThat(response.id()).isEqualTo(salvo.getId());
        assertThat(response.usuarioId()).isEqualTo(CaminhoStub.USUARIO_ID);
        verify(caminhoRepository).save(any(Caminho.class));
    }

    @Test
    @DisplayName("iniciar deve falhar quando aventura nao existe")
    void deveFalharIniciarSemAventura() {
        CaminhoRequest request = CaminhoStub.umRequest();
        when(aventuraRepository.findById(request.aventuraId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aventura nao encontrada");

        verify(caminhoRepository, never()).save(any());
    }

    @Test
    @DisplayName("finalizar deve gravar data de fim e distancia")
    void deveFinalizarCaminho() {
        Caminho caminho = CaminhoStub.umCaminho().build();
        when(caminhoRepository.findById(CaminhoStub.ID)).thenReturn(Optional.of(caminho));
        when(caminhoRepository.save(any(Caminho.class))).thenAnswer(inv -> inv.getArgument(0));

        CaminhoResponse response = service.finalizar(CaminhoStub.ID, 12.5);

        assertThat(response.distanciaTotalKm()).isEqualTo(12.5);
        assertThat(response.finalizadoEm()).isNotNull();
    }

    @Test
    @DisplayName("finalizar deve falhar quando caminho nao existe")
    void deveFalharFinalizarInexistente() {
        when(caminhoRepository.findById("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.finalizar("inexistente", 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Caminho nao encontrado");
    }

    @Test
    @DisplayName("getByAventura deve mapear lista")
    void deveListarPorAventura() {
        when(caminhoRepository.findByAventuraId(AventuraStub.ID))
                .thenReturn(List.of(CaminhoStub.umCaminho().build()));

        List<CaminhoResponse> response = service.getByAventura(AventuraStub.ID);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).aventuraId()).isEqualTo(AventuraStub.ID);
    }

    @Test
    @DisplayName("getByUsuario deve mapear lista")
    void deveListarPorUsuario() {
        when(caminhoRepository.findByUsuarioId(CaminhoStub.USUARIO_ID))
                .thenReturn(List.of(CaminhoStub.umCaminho().build()));

        List<CaminhoResponse> response = service.getByUsuario(CaminhoStub.USUARIO_ID);

        assertThat(response).hasSize(1);
    }
}
