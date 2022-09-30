package model.scambio;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Scambio {
    private String piazza;
    private List<String> luoghi;
    private List<DayOfWeek> giorni;
    private List<IntervalloOrario> intervalliOrari;
    private int scadenzaProposta;

    public Scambio(String piazza) {
        this.piazza = piazza;
    }

    public void setLuoghi(List<String> luoghi) {
        this.luoghi = luoghi;
    }

    public void setGiorni(List<DayOfWeek> giorni) {
        this.giorni = giorni;
    }

    public void setIntervalliOrari(List<IntervalloOrario> intervalliOrari) {
        this.intervalliOrari = intervalliOrari;
    }

    public void setScadenzaProposta(int scadenzaProposta) {
        this.scadenzaProposta = scadenzaProposta;
    }

    public int getScadenzaProposta() {
        return scadenzaProposta;
    }

    public String getPiazza() {
        return piazza;
    }

    public List<String> getLuoghi() {
        return luoghi;
    }

    public List<DayOfWeek> getGiorni() {
        return giorni;
    }

    public List<IntervalloOrario> getIntervalliOrari() {
        return intervalliOrari;
    }

    public ArrayList<String> getOrariScambio() {
        ArrayList<String> orari = new ArrayList<>();
        LocalTime oraTempInizio;
        LocalTime oraTempFine;

        for (IntervalloOrario intervallo : intervalliOrari) {
            oraTempInizio = intervallo.getOraInizio();
            oraTempFine = intervallo.getOraFine();
            do {
                orari.add(oraTempInizio.toString());
                oraTempInizio = oraTempInizio.plusMinutes(30);
            } while (!oraTempInizio.toString().equals(oraTempFine.plusMinutes(30).toString()));
        }
        return orari;
    }
}
