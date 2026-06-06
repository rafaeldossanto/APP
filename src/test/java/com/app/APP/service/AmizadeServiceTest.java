package com.app.APP.service;

import com.app.APP.entity.Amizades;
import com.app.APP.model.dto.request.AmizadeRequest;
import com.app.APP.model.dto.response.AmizadeResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.repository.AmizadesRepository;
import com.app.APP.repository.UsuarioRepository;
import com.app.APP.stub.AmizadeStub;
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
@DisplayName("AmizadeService")
class AmizadeServiceTest {

    @Mock
    private AmizadesRepository amizadesRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AmizadeService service;

    @Test
    @DisplayName("solicitar deve criar amizade pendente quando nao ha relacao previa")
    void deveSolicitarAmizade() {
        AmizadeRequest request = AmizadeStub.umRequest();
        when(usuarioRepository.existsById(request.receptorId())).thenReturn(true);
        when(amizadesRepository.findRelacao(request.solicitanteId(), request.receptorId()))
                .thenReturn(Optional.empty());
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.solicitar(request);

        assertThat(response.solicitanteId()).isEqualTo(request.solicitanteId());
        assertThat(response.status()).isEqualTo(StatusAmizade.PENDENTE);
        verify(amizadesRepository).save(any(Amizades.class));
    }

    @Test
    @DisplayName("solicitar deve falhar quando receptor nao existe")
    void deveFalharReceptorInexistente() {
        AmizadeRequest request = AmizadeStub.umRequest();
        when(usuarioRepository.existsById(request.receptorId())).thenReturn(false);

        assertThatThrownBy(() -> service.solicitar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("receptor nao encontrado");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("solicitar deve falhar quando ja existe relacao")
    void deveFalharRelacaoDuplicada() {
        AmizadeRequest request = AmizadeStub.umRequest();
        when(usuarioRepository.existsById(request.receptorId())).thenReturn(true);
        when(amizadesRepository.findRelacao(request.solicitanteId(), request.receptorId()))
                .thenReturn(Optional.of(AmizadeStub.umaAmizade().build()));

        assertThatThrownBy(() -> service.solicitar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ja existe uma relacao");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("responder deve aceitar solicitacao pendente")
    void deveResponderAceita() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.responder(AmizadeStub.ID, StatusAmizade.ACEITA);

        assertThat(response.status()).isEqualTo(StatusAmizade.ACEITA);
        assertThat(amizade.getRespondidoEm()).isNotNull();
    }

    @Test
    @DisplayName("responder deve falhar quando solicitacao ja foi respondida")
    void deveFalharResponderJaRespondida() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.responder(AmizadeStub.ID, StatusAmizade.RECUSADA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ja foi respondida");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("getPendentes deve filtrar por status PENDENTE")
    void deveListarPendentes() {
        when(amizadesRepository.findByUsuarioIdAndStatus(AmizadeStub.SOLICITANTE_ID, StatusAmizade.PENDENTE))
                .thenReturn(List.of(AmizadeStub.umaAmizade().build()));

        List<AmizadeResponse> response = service.getPendentes(AmizadeStub.SOLICITANTE_ID);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).status()).isEqualTo(StatusAmizade.PENDENTE);
    }

    @Test
    @DisplayName("getAmigos deve filtrar por status ACEITA")
    void deveListarAmigos() {
        when(amizadesRepository.findByUsuarioIdAndStatus(AmizadeStub.SOLICITANTE_ID, StatusAmizade.ACEITA))
                .thenReturn(List.of(AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build()));

        List<AmizadeResponse> response = service.getAmigos(AmizadeStub.SOLICITANTE_ID);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).status()).isEqualTo(StatusAmizade.ACEITA);
    }
}
