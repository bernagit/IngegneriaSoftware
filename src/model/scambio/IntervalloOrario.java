package model.scambio;

import java.time.LocalTime;

public class IntervalloOrario {
    private String oraInizio;
    private String oraFine;
    public IntervalloOrario(String oraInizio, String oraFine) {
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    public boolean isIntersected(IntervalloOrario interval){

        if(this.getOraInizio().isBefore(interval.getOraInizio()) && this.getOraFine().isAfter(interval.getOraFine()))
            return true;
        else if (this.getOraFine().isAfter(interval.getOraFine()) && this.getOraInizio().isBefore(interval.getOraFine()))
            return true;
        else return interval.getOraInizio().isBefore(this.getOraInizio()) && interval.getOraFine().isAfter(this.getOraFine());
    }

    public LocalTime getOraInizio() {
        return LocalTime.parse(oraInizio);
    }

    public LocalTime getOraFine() {
        return LocalTime.parse(oraFine);
    }
}
