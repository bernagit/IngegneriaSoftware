package model.offerta;

import model.gerarchia.CampoNativo;

public class CampoCompilato {
    private CampoNativo campo;
    private String contenuto;

    public CampoCompilato(CampoNativo campo, String contenuto) {
        this.campo = campo;
        this.contenuto = contenuto;
    }

    public CampoNativo getCampo() {
        return campo;
    }

    public String getContenuto() {
        return contenuto;
    }
}
