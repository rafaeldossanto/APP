package com.app.APP.stub;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Regiao;
import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.enums.StatusAventura;
import com.app.APP.model.enums.VisibilidadeAventura;

import java.time.LocalDateTime;

/**
 * Facilitador de testes para Aventura.
 * Cada metodo devolve um builder ja preenchido com valores validos,
 * permitindo uso direto ({@code AventuraStub.umaAventura().build()})
 * ou sobrescrita pontual de campos quando o teste precisar.
 */
public final class AventuraStub {

    public static final String ID = "aventura-1";
    public static final String USUARIO_ID = "usuario-1";
    public static final String REGIAO_ID = "regiao-1";
    public static final String DESTINO = "Pico da Bandeira";

    private AventuraStub() {
    }

    public static Aventura.AventuraBuilder umaAventura() {
        return Aventura.builder()
                .id(ID)
                .usuarioId(USUARIO_ID)
                .regiao(RegiaoStub.umaRegiao().build())
                .destino(DESTINO)
                .status(StatusAventura.PLANEJADA)
                .visibilidade(VisibilidadeAventura.PRIVADA)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now());
    }

    public static AventuraRequest umRequest() {
        return new AventuraRequest(USUARIO_ID, REGIAO_ID, DESTINO, VisibilidadeAventura.PRIVADA);
    }

    public static final class RegiaoStub {

        private RegiaoStub() {
        }

        public static Regiao.RegiaoBuilder umaRegiao() {
            return Regiao.builder()
                    .id(REGIAO_ID)
                    .nome("Serra do Caparao")
                    .descricao("Regiao de montanhas")
                    .latMin(-20.5).latMax(-20.4)
                    .lngMin(-41.9).lngMax(-41.8);
        }
    }
}
