package controller.handlers;

import controller.Handler;
import db.JsonManager;
import model.scambio.IntervalloOrario;
import model.scambio.Scambio;
import model.user.Utente;
import view.View;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InserisciScambio implements Handler {
    private JsonManager jsonManager = JsonManager.getInstance();
    @Override
    public Utente execute(Utente utente, View view) {
        this.inserisciScambio(view);
        return null;
    }

    private void inserisciScambio(View view) {
        Scambio scambio = jsonManager.readScambio();
        boolean modifica = true;
        //se scambio esiste chiedo di modificarlo altrimente lo creo chiedendo la città di scambio
        if (scambio != null) {
            modifica = view.getBoolean("\nScambio già presente, si vuole modificare? ");
            view.print("Città di scambio: " + scambio.getPiazza());
        }
        else{
            String piazza = view.getString("Inserisci piazza di scambio (Citta): ");
            //creazione scambio
            scambio = new Scambio(piazza);
        }
        if (!modifica)
            return;
        //inserimento luoghi
        scambio.setLuoghi(this.inserisciLuoghi(view));

        //inserimento giorni scambio
        scambio.setGiorni(this.inserisciGiorni(view));

        //inserimento intervallo orario
        scambio.setIntervalliOrari(this.inserisciIntervalli(view));

        int scadenzaProposta = view.getIntPos("Inserisci numero giorni durata proposta: ");
        scambio.setScadenzaProposta(scadenzaProposta);

        if (view.getBoolean("Salvare scambio? "))
            jsonManager.writeScambio(scambio);
    }

    private List<String> inserisciLuoghi(View view) {
        boolean inserisciLuoghi;
        List<String> luoghi = new ArrayList<>();
        do {
            String luogo = view.getString("Inserisci luogo: ");
            luoghi.add(luogo);
            inserisciLuoghi = view.getBoolean("Vuoi inserire altri luoghi? ");
        } while (inserisciLuoghi);
        return luoghi;
    }

    private List<DayOfWeek> inserisciGiorni(View view) {
        int day;
        boolean end = false; //controlla lo stop all'input da parte dell'utente
        List<DayOfWeek> days = new ArrayList<>();

        view.print("Inserisci giorni dello scambio  (1=Lunedi, 2=Martedi, ..., 7=Domenica) (0 per terminare)");

        do{
            day = view.getInt("");
            if(day == 0)
                if(days.isEmpty()) view.print("Inserire almeno un giorno");
                else end = true;
            else if(day > 0 && day < 8)
                if(!days.contains(DayOfWeek.of(day)))
                    days.add(DayOfWeek.of(day));
                else view.print("Giorno già inserito");
            else view.print("Inserire un intero compreso tra 1 e 7");
        }while(!end);

        return days;
    }

    private List<IntervalloOrario> inserisciIntervalli(View view) {
        boolean esci;
        List<IntervalloOrario> intervals = new ArrayList<>();
        do {
            IntervalloOrario interval = inserisciIntervallo(view);
            boolean ok = intervals.stream().anyMatch(interval::isIntersected);
            if (ok)
                view.print("Attenzione: l'intervallo inserito interseca altri intervalli.");
            else
                intervals.add(interval);
            esci = view.getBoolean("Vuoi inserire un altro intervallo? ");
        } while (esci);
        return intervals;
    }

    private IntervalloOrario inserisciIntervallo(View view) {
        view.print("Orario iniziale");
        LocalTime oraInizio = inserisciOrario(view);
        boolean intervalloOk;
        LocalTime oraFine;
        do {
            view.print("Orario finale");
            oraFine = inserisciOrario(view);
            intervalloOk = oraInizio.isAfter(oraFine);
            if (intervalloOk)
                view.print("Attenzione: l'orario finale non può essere minore di quello iniziale");
        } while (intervalloOk);
        //controllo se ora iniziale è precedente a ora finale altrimenti richiedo il secondo orario
        return new IntervalloOrario(oraInizio.toString(), oraFine.toString());
    }

    private LocalTime inserisciOrario(View view) {
        //chiedo ora
        int ora = view.getInt("Inserisci ora (0-23): ", 0, 23);
        //chiedo minuti (0 o 30)
        boolean minutiOk;
        int minuti;
        do {
            minuti = view.getInt("Inserisci minuti (0 - 30): ");
            minutiOk = (minuti != 0 && minuti != 30);
            if (minutiOk)
                view.print("Minuti inseriti errati, opzioni disponibili (0 - 30)");
        } while (minutiOk);
        return LocalTime.of(ora, minuti);
    }
}
