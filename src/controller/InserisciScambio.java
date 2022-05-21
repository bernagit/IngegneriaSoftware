package controller;

import model.gerarchia.Gerarchia;
import model.scambio.IntervallOrario;
import model.scambio.IntervalloOrario;
import model.scambio.Scambio;
import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;
import utility.MyMenu;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class InserisciScambio implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.inserisciScambio();
        return null;
    }

    private void inserisciScambio() {
        /*MyMenu menu = new MyMenu("Inserisci scambio");
        List<Gerarchia> gerarchiaList = JsonUtil.getGerarchieLibere();
        ArrayList<String> voci = new ArrayList<>();
        if (gerarchiaList.size() >= 1) {
            for (Gerarchia gerarchia : gerarchiaList) {
                voci.add(gerarchia.getNomeRadice());
            }
            menu.setVoci(voci);
            Gerarchia scelta = gerarchiaList.get(menu.scegli());*/
            String piazza = InputDati.leggiStringaNonVuota("Inserisci piazza di scambio (Citta): ");
            //creazione scambio
            Scambio scambio = new Scambio(piazza);

            //inserimento luoghi
            scambio.setLuoghi(this.inserisciLuoghi());

            //inserimento giorni scambio
            scambio.setGiorni(this.inserisciGiorni());

            //inserimento intervallo orario
            scambio.setIntervalliOrari(this.inserisciIntervalli());

            int scadenzaProposta = InputDati.leggiIntero("Inserisci numero giorni durata proposta: ");
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

    private List<IntervallOrario> inserisciIntervalli() {
        boolean esci;
        List<IntervallOrario> intervals = new ArrayList<>();
        do {
            IntervallOrario interval = inserisciIntervallo();
            boolean ok = intervals.stream().anyMatch(interval::isIntersected);
            if (ok)
                System.out.println("Attenzione: l'intervallo inserito interseca altri intervalli.");
            else
                intervals.add(interval);
            esci = InputDati.yesOrNo("Vuoi inserire un altro intervallo? ");
        } while (esci);
        return intervals;
    }

    private IntervallOrario inserisciIntervallo() {
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
        return new IntervallOrario(oraInizio.truncatedTo(ChronoUnit.MINUTES).toString(), oraFine.truncatedTo(ChronoUnit.MINUTES).toString());
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
