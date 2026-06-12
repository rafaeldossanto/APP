package com.app.APP.stub;

import com.app.APP.entity.Evidencia;
import com.app.APP.entity.PontoInteresse;
import com.app.APP.model.dto.request.EvidenciaRequest;
import com.app.APP.model.dto.request.PontoInteresseRequest;
import com.app.APP.model.enums.TipoEvidencia;
import com.app.APP.model.enums.TipoPonto;

import java.time.LocalDateTime;

/**
 * Facilitador de testes para PontoInteresse e Evidencia.
 * As coordenadas de captura padrao coincidem com as do ponto
 * (distancia 0m), entao a evidencia padrao sempre passa na
 * validacao de proximidade (&lt; 50m).
 */
public final class PontoInteresseStub {

    public static final String ID = "ponto-1";
    public static final String USUARIO_ID = "usuario-1";
    public static final double LATITUDE = -20.4350;
    public static final double LONGITUDE = -41.7920;

    private PontoInteresseStub() {
    }

    public static PontoInteresse.PontoInteresseBuilder umPonto() {
        return PontoInteresse.builder()
                .id(ID)
                .caminho(CaminhoStub.umCaminho().build())
                .usuarioId(USUARIO_ID)
                .tipo(TipoPonto.MIRANTE)
                .nome("Mirante do Vale")
                .descricao("Vista panoramica")
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .criadoEm(LocalDateTime.now());
    }

    public static PontoInteresseRequest umRequest() {
        return new PontoInteresseRequest(
                CaminhoStub.ID, TipoPonto.MIRANTE,
                "Mirante do Vale", "Vista panoramica", LATITUDE, LONGITUDE);
    }

    public static Evidencia.EvidenciaBuilder umaEvidencia() {
        return Evidencia.builder()
                .id("evidencia-1")
                .ponto(umPonto().build())
                .usuarioId(USUARIO_ID)
                .fotoUrl("https://cdn/foto.jpg")
                .tipoEvidencia(TipoEvidencia.VISTA)
                .latCaptura(LATITUDE)
                .lngCaptura(LONGITUDE)
                .distanciaDopontoM(0.0)
                .capturadaNoApp(true)
                .validada(true)
                .criadoEm(LocalDateTime.now());
    }

    /** Evidencia capturada exatamente sobre o ponto (distancia ~0m, valida). */
    public static EvidenciaRequest umRequestEvidenciaProxima() {
        return new EvidenciaRequest(
                ID, "https://cdn/foto.jpg",
                TipoEvidencia.VISTA, LATITUDE, LONGITUDE);
    }

    /** Evidencia capturada longe do ponto (~111km, deve ser rejeitada). */
    public static EvidenciaRequest umRequestEvidenciaLonge() {
        return new EvidenciaRequest(
                ID, "https://cdn/foto.jpg",
                TipoEvidencia.VISTA, LATITUDE + 1.0, LONGITUDE);
    }
}
