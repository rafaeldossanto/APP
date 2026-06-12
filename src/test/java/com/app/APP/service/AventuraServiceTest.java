package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.ParticipanteAventura;
import com.app.APP.entity.Regiao;
import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.enums.StatusAventura;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.ParticipanteAventuraRepository;
import com.app.APP.repository.RegiaoRepository;
import com.app.APP.stub.AventuraStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
@DisplayName("AventuraService")
class AventuraServiceTest {

    @Mock
    private AventuraRepository aventuraRepository;
    @Mock
    private ParticipanteAventuraRepository participanteRepository;
    @Mock
    private RegiaoRepository regiaoRepository;

    @InjectMocks
    private AventuraService service;

    @Test
    @DisplayName("create deve persistir aventura e o participante criador")
    void deveCriarAventuraEParticipante() {
        AventuraRequest request = AventuraStub.umRequest();
        Regiao regiao = AventuraStub.RegiaoStub.umaRegiao().build();
        Aventura salva = AventuraStub.umaAventura().build();

        when(regiaoRepository.findById(request.regiaoId())).thenReturn(Optional.of(regiao));
        when(aventuraRepository.save(any(Aventura.class))).thenReturn(salva);

        AventuraResponse response = service.create(AventuraStub.USUARIO_ID, request);

        assertThat(response.id()).isEqualTo(salva.getId());
        assertThat(response.destino()).isEqualTo(salva.getDestino());
        verify(aventuraRepository).save(any(Aventura.class));
        verify(participanteRepository).save(any(ParticipanteAventura.class));
    }

    @Test
    @DisplayName("create deve falhar quando regiao nao existe")
    void deveFalharQuandoRegiaoInexistente() {
        AventuraRequest request = AventuraStub.umRequest();
        when(regiaoRepository.findById(request.regiaoId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(AventuraStub.USUARIO_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("regiao nao encontrada");

        verify(aventuraRepository, never()).save(any());
        verify(participanteRepository, never()).save(any());
    }

    @Test
    @DisplayName("getById deve retornar aventura existente")
    void deveRetornarPorId() {
        Aventura aventura = AventuraStub.umaAventura().build();
        when(aventuraRepository.findById(AventuraStub.ID)).thenReturn(Optional.of(aventura));

        AventuraResponse response = service.getById(AventuraStub.ID);

        assertThat(response.id()).isEqualTo(AventuraStub.ID);
    }

    @Test
    @DisplayName("getById deve falhar quando aventura nao existe")
    void deveFalharGetByIdInexistente() {
        when(aventuraRepository.findById("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById("inexistente"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aventura nao encontrada");
    }

    @Test
    @DisplayName("getByUsuario deve mapear pagina de aventuras")
    void deveListarPorUsuario() {
        Pageable pageable = PageRequest.of(0, 10);
        when(aventuraRepository.findByUsuarioId(AventuraStub.USUARIO_ID, pageable))
                .thenReturn(new PageImpl<>(List.of(AventuraStub.umaAventura().build())));

        Page<AventuraResponse> response = service.getByUsuario(AventuraStub.USUARIO_ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).usuarioId()).isEqualTo(AventuraStub.USUARIO_ID);
    }

    @Test
    @DisplayName("atualizarStatus deve alterar o status e salvar")
    void deveAtualizarStatus() {
        Aventura aventura = AventuraStub.umaAventura().status(StatusAventura.PLANEJADA).build();
        when(aventuraRepository.findById(AventuraStub.ID)).thenReturn(Optional.of(aventura));
        when(aventuraRepository.save(any(Aventura.class))).thenAnswer(inv -> inv.getArgument(0));

        AventuraResponse response = service.atualizarStatus(AventuraStub.USUARIO_ID, AventuraStub.ID, StatusAventura.CONCLUIDA);

        assertThat(response.status()).isEqualTo(StatusAventura.CONCLUIDA);
        assertThat(aventura.getAtualizadoEm()).isNotNull();
    }

    @Test
    @DisplayName("adicionarParticipante deve persistir quando ainda nao participa")
    void deveAdicionarParticipante() {
        Aventura aventura = AventuraStub.umaAventura().build();
        when(aventuraRepository.findById(AventuraStub.ID)).thenReturn(Optional.of(aventura));
        when(participanteRepository.existsByAventuraIdAndUsuarioId(AventuraStub.ID, "usuario-9"))
                .thenReturn(false);

        service.adicionarParticipante(AventuraStub.ID, "usuario-9");

        ArgumentCaptor<ParticipanteAventura> captor = ArgumentCaptor.forClass(ParticipanteAventura.class);
        verify(participanteRepository).save(captor.capture());
        assertThat(captor.getValue().getUsuarioId()).isEqualTo("usuario-9");
    }

    @Test
    @DisplayName("adicionarParticipante deve falhar quando usuario ja participa")
    void deveFalharParticipanteDuplicado() {
        Aventura aventura = AventuraStub.umaAventura().build();
        when(aventuraRepository.findById(AventuraStub.ID)).thenReturn(Optional.of(aventura));
        when(participanteRepository.existsByAventuraIdAndUsuarioId(AventuraStub.ID, "usuario-9"))
                .thenReturn(true);

        assertThatThrownBy(() -> service.adicionarParticipante(AventuraStub.ID, "usuario-9"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ja participa");

        verify(participanteRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete deve remover aventura existente")
    void deveDeletar() {
        Aventura aventura = AventuraStub.umaAventura().build();
        when(aventuraRepository.findById(AventuraStub.ID)).thenReturn(Optional.of(aventura));

        service.delete(AventuraStub.USUARIO_ID, AventuraStub.ID);

        verify(aventuraRepository).delete(aventura);
    }
}
