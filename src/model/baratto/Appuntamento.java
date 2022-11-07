package model.baratto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Appuntamento {
    private String luogo;
    private String dataOra;
    private DayOfWeek giorno;

    public Appuntamento(String luogo, String dataOra, DayOfWeek giorno) {
        this.luogo = luogo;
        this.dataOra = dataOra;
        this.giorno = giorno;
    }
    public String getLuogo() {
        return luogo;
    }

    public LocalTime getDataOra() {
        return LocalTime.parse(dataOra);
    }

    public DayOfWeek getGiorno() {
        return giorno;
    }
    public boolean equals(Appuntamento appuntamento){
        return this.getDataOra().equals(appuntamento.getDataOra())
                && this.giorno.equals(appuntamento.getGiorno())
                && this.luogo.equals(appuntamento.getLuogo());
    }
}
