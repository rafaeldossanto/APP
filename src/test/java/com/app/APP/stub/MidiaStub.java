package com.app.APP.stub;

import com.app.APP.entity.Midia;
import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.enums.TipoMidia;

import java.time.LocalDateTime;

/**
 * Facilitador de testes para Midia (metadados — APP nao guarda binario).
 */
public final class MidiaStub {

    public static final String ID = "midia-1";
    public static final String USUARIO_ID = "usuario-1";
    public static final String URL = "https://cdn/midia.jpg";

    private MidiaStub() {
    }

    public static Midia.MidiaBuilder umaMidia() {
        return Midia.builder()
                .id(ID)
                .aventura(AventuraStub.umaAventura().build())
                .caminho(CaminhoStub.umCaminho().build())
                .usuarioId(USUARIO_ID)
                .tipo(TipoMidia.FOTO)
                .url(URL)
                .latCaptura(-20.43)
                .lngCaptura(-41.79)
                .distanciaNaCapturaKm(1.5)
                .percentualNoCaminho(0.30)
                .capturadaEm(LocalDateTime.now());
    }

    /** Request com caminho associado. */
    public static MidiaRequest umRequest() {
        return new MidiaRequest(
                AventuraStub.ID, CaminhoStub.ID, TipoMidia.FOTO,
                URL, -20.43, -41.79, 1.5, 0.30);
    }

    /** Request avulso (sem caminho). */
    public static MidiaRequest umRequestSemCaminho() {
        return new MidiaRequest(
                AventuraStub.ID, null, TipoMidia.FOTO,
                URL, -20.43, -41.79, 1.5, 0.30);
    }
}
