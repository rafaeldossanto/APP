package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.entity.Midia;
import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.dto.response.MidiaResponse;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.CaminhoRepository;
import com.app.APP.repository.MidiaRepository;
import com.app.APP.stub.AventuraStub;
import com.app.APP.stub.CaminhoStub;
import com.app.APP.stub.MidiaStub;
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
@DisplayName("MidiaService")
class MidiaServiceTest {

    @Mock
    private MidiaRepository midiaRepository;
    @Mock
    private AventuraRepository aventuraRepository;
    @Mock
    private CaminhoRepository caminhoRepository;

    @InjectMocks
    private MidiaService service;

    @Test
    @DisplayName("salvar deve persistir midia com caminho associado")
    void deveSalvarComCaminho() {
        MidiaRequest request = MidiaStub.umRequest();
        Aventura aventura = AventuraStub.umaAventura().build();
        Caminho caminho = CaminhoStub.umCaminho().build();
        when(aventuraRepository.findById(request.aventuraId())).thenReturn(Optional.of(aventura));
        when(caminhoRepository.findById(request.caminhoId())).thenReturn(Optional.of(caminho));
        when(midiaRepository.save(any(Midia.class))).thenAnswer(inv -> inv.getArgument(0));

        MidiaResponse response = service.salvar(request);

        assertThat(response.url()).isEqualTo(request.url());
        assertThat(response.caminhoId()).isEqualTo(CaminhoStub.ID);
        verify(midiaRepository).save(any(Midia.class));
    }

    @Test
    @DisplayName("salvar deve persistir midia avulsa (sem caminho)")
    void deveSalvarSemCaminho() {
        MidiaRequest request = MidiaStub.umRequestSemCaminho();
        Aventura aventura = AventuraStub.umaAventura().build();
        when(aventuraRepository.findById(request.aventuraId())).thenReturn(Optional.of(aventura));
        when(midiaRepository.save(any(Midia.class))).thenAnswer(inv -> inv.getArgument(0));

        MidiaResponse response = service.salvar(request);

        assertThat(response.caminhoId()).isNull();
        verify(caminhoRepository, never()).findById(any());
    }

    @Test
    @DisplayName("salvar deve falhar quando aventura nao existe")
    void deveFalharSemAventura() {
        MidiaRequest request = MidiaStub.umRequest();
        when(aventuraRepository.findById(request.aventuraId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aventura nao encontrada");

        verify(midiaRepository, never()).save(any());
    }

    @Test
    @DisplayName("salvar deve falhar quando caminho informado nao existe")
    void deveFalharComCaminhoInexistente() {
        MidiaRequest request = MidiaStub.umRequest();
        Aventura aventura = AventuraStub.umaAventura().build();
        when(aventuraRepository.findById(request.aventuraId())).thenReturn(Optional.of(aventura));
        when(caminhoRepository.findById(request.caminhoId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Caminho nao encontrado");

        verify(midiaRepository, never()).save(any());
    }

    @Test
    @DisplayName("getByAventura deve mapear pagina")
    void deveListarPorAventura() {
        Pageable pageable = PageRequest.of(0, 10);
        when(midiaRepository.findByAventuraId(AventuraStub.ID, pageable))
                .thenReturn(new PageImpl<>(List.of(MidiaStub.umaMidia().build())));

        Page<MidiaResponse> response = service.getByAventura(AventuraStub.ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).aventuraId()).isEqualTo(AventuraStub.ID);
    }

    @Test
    @DisplayName("getByCaminho deve mapear pagina")
    void deveListarPorCaminho() {
        Pageable pageable = PageRequest.of(0, 10);
        when(midiaRepository.findByCaminhoId(CaminhoStub.ID, pageable))
                .thenReturn(new PageImpl<>(List.of(MidiaStub.umaMidia().build())));

        Page<MidiaResponse> response = service.getByCaminho(CaminhoStub.ID, pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("delete deve remover midia existente")
    void deveDeletar() {
        Midia midia = MidiaStub.umaMidia().build();
        when(midiaRepository.findById(MidiaStub.ID)).thenReturn(Optional.of(midia));

        service.delete(MidiaStub.ID);

        verify(midiaRepository).delete(midia);
    }

    @Test
    @DisplayName("delete deve falhar quando midia nao existe")
    void deveFalharDeletarInexistente() {
        when(midiaRepository.findById("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete("inexistente"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Midia nao encontrada");
    }
}
