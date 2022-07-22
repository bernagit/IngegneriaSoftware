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
        } else {
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
        boolean errore = false;
        List<DayOfWeek> days = new ArrayList<>();
        System.out.println("Inserisci giorni dello scambio (1=Lunedi, 2=Martedi, ..., 7=Domenica)");
        do {
            String dayString = InputDati.leggiStringaNonVuota("Giorni [1...7]: ");
            for (int i = 0; i < dayString.length(); i++) {
                char ch = dayString.charAt(i);
                if (Character.isDigit(ch)) {
                    int num = Character.getNumericValue(ch);
                    if (num < 8) {
                        if (!days.contains(DayOfWeek.of(num))) {
                            days.add(DayOfWeek.of(num));
                            errore = false;
                        }
                    } else
                        errore = true;
                }
            }
            if (errore)
                System.out.println("Errore nell'inserimento dei giorni... range disponibile [1...7]");
        } while (errore);
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
