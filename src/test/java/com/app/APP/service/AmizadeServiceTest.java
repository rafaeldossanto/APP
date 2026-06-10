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

    // ---- responder ----

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

    // ---- cancelar / desfazer ----

    @Test
    @DisplayName("cancelarSolicitacao deve remover uma solicitacao pendente")
    void deveCancelarSolicitacao() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        service.cancelarSolicitacao(AmizadeStub.ID);

        verify(amizadesRepository).delete(amizade);
    }

    @Test
    @DisplayName("cancelarSolicitacao deve falhar quando nao esta pendente")
    void deveFalharCancelarNaoPendente() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.cancelarSolicitacao(AmizadeStub.ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pendente");

        verify(amizadesRepository, never()).delete(any());
    }

    @Test
    @DisplayName("desfazerAmizade deve remover uma amizade aceita")
    void deveDesfazerAmizade() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        service.desfazerAmizade(AmizadeStub.ID);

        verify(amizadesRepository).delete(amizade);
    }

    @Test
    @DisplayName("desfazerAmizade deve falhar quando nao esta aceita")
    void deveFalharDesfazerNaoAceita() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.PENDENTE).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.desfazerAmizade(AmizadeStub.ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("aceita");

        verify(amizadesRepository, never()).delete(any());
    }

    // ---- bloquear / desbloquear ----

    @Test
    @DisplayName("bloquear deve marcar relacao existente como BLOQUEADA e registrar quem bloqueou")
    void deveBloquearRelacaoExistente() {
        AmizadeRequest request = AmizadeStub.umRequest();
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(usuarioRepository.existsById(request.receptorId())).thenReturn(true);
        when(amizadesRepository.findRelacao(request.solicitanteId(), request.receptorId()))
                .thenReturn(Optional.of(amizade));
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.bloquear(request);

        assertThat(response.status()).isEqualTo(StatusAmizade.BLOQUEADA);
        assertThat(response.bloqueadoPor()).isEqualTo(request.solicitanteId());
    }

    @Test
    @DisplayName("bloquear deve criar relacao BLOQUEADA quando nao havia relacao previa")
    void deveBloquearSemRelacaoPrevia() {
        AmizadeRequest request = AmizadeStub.umRequest();
        when(usuarioRepository.existsById(request.receptorId())).thenReturn(true);
        when(amizadesRepository.findRelacao(request.solicitanteId(), request.receptorId()))
                .thenReturn(Optional.empty());
        when(amizadesRepository.save(any(Amizades.class))).thenAnswer(inv -> inv.getArgument(0));

        AmizadeResponse response = service.bloquear(request);

        assertThat(response.status()).isEqualTo(StatusAmizade.BLOQUEADA);
        verify(amizadesRepository).save(any(Amizades.class));
    }

    @Test
    @DisplayName("bloquear deve falhar quando o alvo nao existe")
    void deveFalharBloquearAlvoInexistente() {
        AmizadeRequest request = AmizadeStub.umRequest();
        when(usuarioRepository.existsById(request.receptorId())).thenReturn(false);

        assertThatThrownBy(() -> service.bloquear(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao encontrado");

        verify(amizadesRepository, never()).save(any());
    }

    @Test
    @DisplayName("desbloquear deve remover uma relacao bloqueada")
    void deveDesbloquear() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.BLOQUEADA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        service.desbloquear(AmizadeStub.ID);

        verify(amizadesRepository).delete(amizade);
    }

    @Test
    @DisplayName("desbloquear deve falhar quando a relacao nao esta bloqueada")
    void deveFalharDesbloquearNaoBloqueada() {
        Amizades amizade = AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build();
        when(amizadesRepository.findById(AmizadeStub.ID)).thenReturn(Optional.of(amizade));

        assertThatThrownBy(() -> service.desbloquear(AmizadeStub.ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("bloqueada");

        verify(amizadesRepository, never()).delete(any());
    }

    // ---- listagens paginadas ----

    @Test
    @DisplayName("getPendentes deve trazer as solicitacoes recebidas (receptor) pendentes")
    void deveListarRecebidasPendentes() {
        when(amizadesRepository.findByReceptorIdAndStatus(AmizadeStub.RECEPTOR_ID, StatusAmizade.PENDENTE, pageable))
                .thenReturn(new PageImpl<>(List.of(AmizadeStub.umaAmizade().build())));

        Page<AmizadeResponse> response = service.getPendentes(AmizadeStub.RECEPTOR_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).status()).isEqualTo(StatusAmizade.PENDENTE);
    }

    @Test
    @DisplayName("getEnviadas deve trazer as solicitacoes enviadas (solicitante) pendentes")
    void deveListarEnviadasPendentes() {
        when(amizadesRepository.findBySolicitanteIdAndStatus(AmizadeStub.SOLICITANTE_ID, StatusAmizade.PENDENTE, pageable))
                .thenReturn(new PageImpl<>(List.of(AmizadeStub.umaAmizade().build())));

        Page<AmizadeResponse> response = service.getEnviadas(AmizadeStub.SOLICITANTE_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).solicitanteId()).isEqualTo(AmizadeStub.SOLICITANTE_ID);
    }

    @Test
    @DisplayName("getAmigos deve filtrar por status ACEITA")
    void deveListarAmigos() {
        when(amizadesRepository.findByUsuarioIdAndStatus(AmizadeStub.SOLICITANTE_ID, StatusAmizade.ACEITA, pageable))
                .thenReturn(new PageImpl<>(List.of(AmizadeStub.umaAmizade().status(StatusAmizade.ACEITA).build())));

        Page<AmizadeResponse> response = service.getAmigos(AmizadeStub.SOLICITANTE_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).status()).isEqualTo(StatusAmizade.ACEITA);
    }
}
