package com.app.APP.service;

import com.app.APP.entity.Usuario;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Busca de usuarios pelo codigoUsuario publico (ex.: "rafael#1"), para o fluxo
 * de adicionar amigos. Expoe apenas dados publicos (nome + codigo), nunca email
 * nem id interno.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioBuscaService {

    private static final int LIMITE_AUTOCOMPLETE = 10;

    private final UsuarioRepository usuarioRepository;

    /** Busca exata: usada quando o usuario digita o codigo completo do amigo. */
    public UsuarioPublicoResponse buscarPorCodigo(String codigoUsuario) {
        Usuario usuario = usuarioRepository.findByCodigoUsuario(codigoUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        return toPublico(usuario);
    }

    /**
     * Autocomplete: lista usuarios cujo codigo comeca pelo termo digitado,
     * limitado para nao retornar a base inteira.
     */
    public List<UsuarioPublicoResponse> autocomplete(String termo) {
        return usuarioRepository.buscarPorPrefixoCodigo(termo).stream()
                .limit(LIMITE_AUTOCOMPLETE)
                .map(this::toPublico)
                .toList();
    }

    private UsuarioPublicoResponse toPublico(Usuario usuario) {
        return UsuarioPublicoResponse.builder()
                .codigoUsuario(usuario.getCodigoUsuario())
                .nome(usuario.getNome())
                .build();
    }
}
