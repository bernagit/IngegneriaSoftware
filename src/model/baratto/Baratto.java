package model.baratto;

import model.offerta.Offerta;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Baratto {
    private Offerta offertaA;
    private Offerta offertaB;
    private String dataOraBaratto;
    private String decisore;
    private Appuntamento appuntamento;

    public Baratto(Offerta offertaA, Offerta offertaB, String dataOraBaratto) {
        this.offertaA = offertaA;
        this.offertaB = offertaB;
        this.dataOraBaratto = dataOraBaratto;
    }

    public Offerta getOffertaA() {
        return offertaA;
    }

    public Offerta getOffertaB() {
        return offertaB;
    }

    public LocalDateTime getDataOraBaratto() {
        return LocalDateTime.parse(dataOraBaratto);
    }

    public void setAppuntamento(Appuntamento appuntamento) {
        this.appuntamento = appuntamento;
    }

    public void setOffertaA(Offerta offertaA) {
        this.offertaA = offertaA;
    }

    public void setOffertaB(Offerta offertaB) {
        this.offertaB = offertaB;
    }

    public String getUtenteA() {
        return offertaA.getAutore();
    }

    public String getUtenteB() {
        return offertaB.getAutore();
    }

    public Appuntamento getAppuntamento() {
        return appuntamento;
    }

    public String getDecisore() {
        return decisore;
    }

    public void setDecisore(String decisore) {
        this.decisore = decisore;
    }
}
