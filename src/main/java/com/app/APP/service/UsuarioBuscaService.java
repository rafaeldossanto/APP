package com.app.APP.service;

import com.app.APP.entity.Usuario;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioBuscaService {

    private static final int LIMITE_AUTOCOMPLETE = 10;

    private final UsuarioRepository usuarioRepository;

    public UsuarioPublicoResponse buscarPorCodigo(String codigoUsuario) {
        Usuario usuario = usuarioRepository.findByCodigoUsuario(codigoUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        return toPublico(usuario);
    }

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
