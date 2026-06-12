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
@DisplayName("AmizadeService")
class AmizadeServiceTest {

    @Mock
    private AmizadesRepository amizadesRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AmizadeService service;

    private final Pageable pageable = PageRequest.of(0, 10);

    // ---- solicitar ----

    @Test
    @DisplayName("solicitar deve criar amizade pendente com o solicitante do token")
    void deveSolicitarAmizade() {
        AmizadeRequest request = AmizadeStub.umRequest();
        when(usuarioRepository.existsById(AmizadeStub.RECEPTOR_ID)).thenReturn(true);
        when(amizadesRepository.findRelacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID)).thenReturn(Optional.empty());
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.solicitar(AmizadeStub.SOLICITANTE_ID, request);

        assertThat(response.solicitanteId()).isEqualTo(AmizadeStub.SOLICITANTE_ID);
        assertThat(response.receptorId()).isEqualTo(AmizadeStub.RECEPTOR_ID);
        assertThat(response.status()).isEqualTo(StatusAmizade.PENDENTE);
        verify(amizadesRepository).save(any(Amizades.class));
    }

    @Test
    @DisplayName("solicitar deve falhar quando receptor nao existe")
    void deveFalharReceptorInexistente() {
        when(usuarioRepository.existsById(AmizadeStub.RECEPTOR_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.solicitar(AmizadeStub.SOLICITANTE_ID, AmizadeStub.umRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("receptor nao encontrado");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("solicitar deve falhar quando ja existe relacao")
    void deveFalharRelacaoDuplicada() {
        when(usuarioRepository.existsById(AmizadeStub.RECEPTOR_ID)).thenReturn(true);
        when(amizadesRepository.findRelacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID))
                .thenReturn(Optional.of(AmizadeStub.umaAmizade().build()));

        assertThatThrownBy(() -> service.solicitar(AmizadeStub.SOLICITANTE_ID, AmizadeStub.umRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ja existe uma relacao");

        verify(amizadesRepository, never()).save(any());
    }

    // ---- responder ----

    @Test
    @DisplayName("responder deve aceitar quando e o destinatario")
    void deveResponderAceita() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.responder(AmizadeStub.RECEPTOR_ID, AmizadeStub.ID, StatusAmizade.ACEITA);

        assertThat(response.status()).isEqualTo(StatusAmizade.ACEITA);
        assertThat(amizade.getRespondidoEm()).isNotNull();
    }

    @Test
    @DisplayName("responder deve falhar quando nao e o destinatario")
    void deveFalharResponderNaoDestinatario() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.responder(AmizadeStub.SOLICITANTE_ID, AmizadeStub.ID, StatusAmizade.ACEITA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("destinatario");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("responder deve falhar quando ja respondida")
    void deveFalharResponderJaRespondida() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.responder(AmizadeStub.RECEPTOR_ID, AmizadeStub.ID, StatusAmizade.RECUSADA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ja foi respondida");

        verify(amizadesRepository, never()).save(any());
    }

    // ---- cancelar / desfazer ----

    @Test
    @DisplayName("cancelarSolicitacao deve remover quando e o solicitante")
    void deveCancelarSolicitacao() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        service.cancelarSolicitacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.ID);

        verify(amizadesRepository).delete(amizade);
    }

    @Test
    @DisplayName("cancelarSolicitacao deve falhar quando nao e o solicitante")
    void deveFalharCancelarNaoSolicitante() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.cancelarSolicitacao(AmizadeStub.RECEPTOR_ID, AmizadeStub.ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quem enviou");

        verify(amizadesRepository, never()).delete(any());
    }

    @Test
    @DisplayName("desfazerAmizade deve remover quando participante e aceita")
    void deveDesfazerAmizade() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        service.desfazerAmizade(AmizadeStub.RECEPTOR_ID, AmizadeStub.ID);

        verify(amizadesRepository).delete(amizade);
    }

    @Test
    @DisplayName("desfazerAmizade deve falhar quando nao participa")
    void deveFalharDesfazerNaoParticipa() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.desfazerAmizade("estranho", AmizadeStub.ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao participa");

        verify(amizadesRepository, never()).delete(any());
    }

    // ---- bloquear / desbloquear ----

    @Test
    @DisplayName("bloquear deve marcar relacao existente como BLOQUEADA e registrar quem bloqueou")
    void deveBloquearRelacaoExistente() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(usuarioRepository.existsById(AmizadeStub.RECEPTOR_ID)).thenReturn(true);
        when(amizadesRepository.findRelacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID)).thenReturn(Optional.of(amizade));
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.bloquear(AmizadeStub.SOLICITANTE_ID, AmizadeStub.umRequest());

        assertThat(response.status()).isEqualTo(StatusAmizade.BLOQUEADA);
        assertThat(response.bloqueadoPor()).isEqualTo(AmizadeStub.SOLICITANTE_ID);
    }

    @Test
    @DisplayName("bloquear deve criar relacao BLOQUEADA quando nao havia relacao previa")
    void deveBloquearSemRelacaoPrevia() {
        when(usuarioRepository.existsById(AmizadeStub.RECEPTOR_ID)).thenReturn(true);
        when(amizadesRepository.findRelacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID)).thenReturn(Optional.empty());
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.bloquear(AmizadeStub.SOLICITANTE_ID, AmizadeStub.umRequest());

        assertThat(response.status()).isEqualTo(StatusAmizade.BLOQUEADA);
        verify(amizadesRepository).save(any(Amizades.class));
    }

    @Test
    @DisplayName("bloquear deve falhar quando o alvo nao existe")
    void deveFalharBloquearAlvoInexistente() {
        when(usuarioRepository.existsById(AmizadeStub.RECEPTOR_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.bloquear(AmizadeStub.SOLICITANTE_ID, AmizadeStub.umRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao encontrado");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("desbloquear deve remover quando foi quem bloqueou")
    void deveDesbloquear() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.BLOQUEADA).bloqueadoPor(AmizadeStub.SOLICITANTE_ID).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        service.desbloquear(AmizadeStub.SOLICITANTE_ID, AmizadeStub.ID);

        verify(amizadesRepository).delete(amizade);
    }

    @Test
    @DisplayName("desbloquear deve falhar quando nao foi quem bloqueou")
    void deveFalharDesbloquearNaoBloqueador() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.BLOQUEADA).bloqueadoPor(AmizadeStub.SOLICITANTE_ID).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.desbloquear(AmizadeStub.RECEPTOR_ID, AmizadeStub.ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quem bloqueou");

        verify(amizadesRepository, never()).delete(any());
    }

    // ---- listagens paginadas ----

    @Test
    @DisplayName("getPendentes traz as recebidas pendentes")
    void deveListarRecebidasPendentes() {
        when(amizadesRepository.findByReceptorIdAndStatus(AmizadeStub.RECEPTOR_ID, StatusAmizade.PENDENTE, pageable))
                .thenReturn(new PageImpl<>(List.of(AmizadeStub.umaAmizade().build())));

        Page<AmizadeResponse> response = service.getPendentes(AmizadeStub.RECEPTOR_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("getEnviadas traz as enviadas pendentes")
    void deveListarEnviadasPendentes() {
        when(amizadesRepository.findBySolicitanteIdAndStatus(AmizadeStub.SOLICITANTE_ID, StatusAmizade.PENDENTE, pageable))
                .thenReturn(new PageImpl<>(List.of(AmizadeStub.umaAmizade().build())));

        Page<AmizadeResponse> response = service.getEnviadas(AmizadeStub.SOLICITANTE_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("getAmigos filtra por ACEITA")
    void deveListarAmigos() {
        when(amizadesRepository.findByUsuarioIdAndStatus(AmizadeStub.SOLICITANTE_ID, StatusAmizade.ACEITA, pageable))
                .thenReturn(new PageImpl<>(List.of(AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build())));

        Page<AmizadeResponse> response = service.getAmigos(AmizadeStub.SOLICITANTE_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).status()).isEqualTo(StatusAmizade.ACEITA);
    }

    // ---- sao-amigos ----

    @Test
    @DisplayName("saoAmigos deve ser true quando ha relacao ACEITA")
    void deveDizerQueSaoAmigos() {
        when(amizadesRepository.findRelacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID))
                .thenReturn(Optional.of(AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build()));

        assertThat(service.saoAmigos(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID)).isTrue();
    }

    @Test
    @DisplayName("saoAmigos deve ser false quando nao ha relacao aceita")
    void deveDizerQueNaoSaoAmigos() {
        when(amizadesRepository.findRelacao(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID)).thenReturn(Optional.empty());

        assertThat(service.saoAmigos(AmizadeStub.SOLICITANTE_ID, AmizadeStub.RECEPTOR_ID)).isFalse();
    }
}
