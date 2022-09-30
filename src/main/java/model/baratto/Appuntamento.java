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

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("\nLuogo: ").append(luogo)
                .append("\nOra: ").append(dataOra)
                .append("\nGiorno: ").append(giorno.name());
        return str.toString();
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
