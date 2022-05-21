package model.scambio;

import com.sun.jdi.IncompatibleThreadStateException;

import java.time.LocalTime;

public class IntervalloOrario {
    private LocalTime oraInizio;
    private LocalTime oraFine;
    public IntervalloOrario(LocalTime oraInizio, LocalTime oraFine) {
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    public boolean isIntersected(IntervalloOrario interval){
        if(this.oraInizio.isBefore(interval.oraInizio) && this.oraFine.isAfter(interval.oraInizio))
            return true;
        else if (this.oraFine.isAfter(interval.oraFine) && this.oraInizio.isBefore(interval.oraFine))
            return true;
        else return interval.oraInizio.isBefore(this.oraInizio) && interval.oraFine.isAfter(this.oraFine);
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }
}
