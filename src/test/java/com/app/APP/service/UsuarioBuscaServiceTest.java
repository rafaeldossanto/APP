package com.app.APP.service;

import com.app.APP.entity.Usuario;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioBuscaService")
class UsuarioBuscaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioBuscaService service;

    private Usuario usuario(String codigo, String nome) {
        Usuario u = new Usuario();
        ReflectionTestUtils.setField(u, "codigoUsuario", codigo);
        ReflectionTestUtils.setField(u, "nome", nome);
        ReflectionTestUtils.setField(u, "email", "secreto@trilha.com");
        return u;
    }

    @Test
    @DisplayName("buscarPorCodigo deve retornar a visao publica (sem email)")
    void deveBuscarPorCodigo() {
        when(usuarioRepository.findByCodigoUsuario("rafael#1"))
                .thenReturn(Optional.of(usuario("rafael#1", "Rafael")));

        UsuarioPublicoResponse response = service.buscarPorCodigo("rafael#1");

        assertThat(response.codigoUsuario()).isEqualTo("rafael#1");
        assertThat(response.nome()).isEqualTo("Rafael");
    }

    @Test
    @DisplayName("buscarPorCodigo deve falhar quando usuario nao existe")
    void deveFalharBuscaInexistente() {
        when(usuarioRepository.findByCodigoUsuario("inexistente#9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorCodigo("inexistente#9"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao encontrado");
    }

    @Test
    @DisplayName("autocomplete deve mapear os resultados do prefixo")
    void deveAutocompletar() {
        when(usuarioRepository.buscarPorPrefixoCodigo("raf"))
                .thenReturn(List.of(usuario("rafael#1", "Rafael"), usuario("rafaela#2", "Rafaela")));

        List<UsuarioPublicoResponse> response = service.autocomplete("raf");

        assertThat(response).hasSize(2);
        assertThat(response.get(0).codigoUsuario()).isEqualTo("rafael#1");
    }
}
