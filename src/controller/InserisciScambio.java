package controller;

import model.scambio.IntervalloOrario;
import model.scambio.Scambio;
import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InserisciScambio implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.inserisciScambio();
        return null;
    }

    private void inserisciScambio() {
        Scambio scambio = JsonUtil.readScambio();
        boolean modifica = true;
        //se scambio esiste chiedo di modificarlo altrimente lo creo chiedendo la città di scambio
        if (scambio != null) {
            modifica = InputDati.yesOrNo("\nScambio già presente, si vuole modificare? ");
            System.out.println("Città di scambio: " + scambio.getPiazza());
        }
        else{
            String piazza = InputDati.leggiStringaNonVuota("Inserisci piazza di scambio (Citta): ");
            //creazione scambio
            scambio = new Scambio(piazza);
        }
        if (!modifica)
            return;
        //inserimento luoghi
        scambio.setLuoghi(this.inserisciLuoghi());

        //inserimento giorni scambio
        scambio.setGiorni(this.inserisciGiorni());

        //inserimento intervallo orario
        scambio.setIntervalliOrari(this.inserisciIntervalli());

        int scadenzaProposta = InputDati.leggiInteroPositivo("Inserisci numero giorni durata proposta: ");
        scambio.setScadenzaProposta(scadenzaProposta);

        if (InputDati.yesOrNo("Salvare scambio? "))
            JsonUtil.writeScambio(scambio);
        /*} else
            System.out.println("\nNon sono presenti Gerarchie per cui inserire scambi...");*/
    }

    private List<String> inserisciLuoghi() {
        boolean inserisciLuoghi;
        List<String> luoghi = new ArrayList<>();
        do {
            String luogo = InputDati.leggiStringaNonVuota("Inserisci luogo: ");
            luoghi.add(luogo);
            inserisciLuoghi = InputDati.yesOrNo("Vuoi inserire altri luoghi? ");
        } while (inserisciLuoghi);
        return luoghi;
    }

    private List<DayOfWeek> inserisciGiorni() {
        int day;
        boolean end = false; //controlla lo stop all'input da parte dell'utente
        List<DayOfWeek> days = new ArrayList<>();

        System.out.println("Inserisci giorni dello scambio  (1=Lunedi, 2=Martedi, ..., 7=Domenica) (0 per terminare)");

        do{
            day = InputDati.leggiIntero("");
            if(day == 0)
                if(days.isEmpty()) System.out.println("Inserire almeno un giorno");
                else end = true;
            else if(day > 0 && day < 8)
                if(!days.contains(DayOfWeek.of(day)))
                    days.add(DayOfWeek.of(day));
                else System.out.println("Giorno già inserito");
            else System.out.println("Inserire un intero compreso tra 1 e 7");
        }while(!end);

        return days;
    }

    private List<IntervalloOrario> inserisciIntervalli() {
        boolean esci;
        List<IntervalloOrario> intervals = new ArrayList<>();
        do {
            IntervalloOrario interval = inserisciIntervallo();
            boolean ok = intervals.stream().anyMatch(interval::isIntersected);
            if (ok)
                System.out.println("Attenzione: l'intervallo inserito interseca altri intervalli.");
            else
                intervals.add(interval);
            esci = InputDati.yesOrNo("Vuoi inserire un altro intervallo? ");
        } while (esci);
        return intervals;
    }

    private IntervalloOrario inserisciIntervallo() {
        System.out.println("Orario iniziale");
        LocalTime oraInizio = inserisciOrario();
        boolean intervalloOk;
        LocalTime oraFine;
        do {
            System.out.println("Orario finale");
            oraFine = inserisciOrario();
            intervalloOk = oraInizio.isAfter(oraFine);
            if (intervalloOk)
                System.out.println("Attenzione: l'orario finale non può essere minore di quello iniziale");
        } while (intervalloOk);
        //controllo se ora iniziale è precedente a ora finale altrimenti richiedo il secondo orario
        return new IntervalloOrario(oraInizio.toString(), oraFine.toString());
    }

    private LocalTime inserisciOrario() {
        //chiedo ora
        int ora = InputDati.leggiIntero("Inserisci ora (0-23): ", 0, 23);
        //chiedo minuti (0 o 30)
        boolean minutiOk;
        int minuti;
        do {
            minuti = InputDati.leggiIntero("Inserisci minuti (0 - 30): ");
            minutiOk = (minuti != 0 && minuti != 30);
            if (minutiOk)
                System.out.println("Minuti inseriti errati, opzioni disponibili (0 - 30)");
        } while (minutiOk);
        return LocalTime.of(ora, minuti);
    }
}
