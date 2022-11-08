package controller.handlers;

import controller.ExitException;
import controller.Handler;
import db.JsonManager;
import model.baratto.Appuntamento;
import model.baratto.Baratto;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.scambio.Scambio;
import model.user.Utente;
import view.View;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccettaBaratto implements Handler {
    @Override
    public Utente execute(Utente utente, View view) throws ExitException {
        this.accettaBaratto(utente, view);
        return null;
    }

    private void accettaBaratto(Utente utente, View view) {
        Scambio scambio = JsonManager.readScambio();
        //selezione offerta da accettare
        Baratto baratto = selezionaBaratto(utente, scambio, view);
        if (baratto == null)
            return;

        //chiedo conferma dopo aver mostrato le info dell'offerta
        Offerta offertaBaratto = baratto.getOffertaA();
        view.printOfferta(offertaBaratto);
        boolean accetta = view.getBoolean("sei sicuro di voler accettare l'offerta: " + offertaBaratto.getTitolo() + " ? ");

        if (!accetta)
            return;

        baratto.setAppuntamento(this.inserisciAppuntamento(scambio, view));
        //conferma prima del salvataggio
        boolean save = view.getBoolean("sei sicuro di voler salvare l'appuntamento? ");
        if (!save) {
            view.print("Appuntamento non salvato!");
            return;
        }
        //cambio stato offerta
        Offerta offertaA = baratto.getOffertaA();
        Offerta offertaB = baratto.getOffertaB();
        offertaA.setStatoCorrente(StatoOfferta.IN_SCAMBIO);
        offertaB.setStatoCorrente(StatoOfferta.IN_SCAMBIO);
        baratto.setOffertaA(offertaA);
        baratto.setOffertaB(offertaB);
        baratto.setDecisore(offertaB.getAutore());
        //salvataggio dati
        JsonManager.writeOfferta(offertaA);
        JsonManager.writeOfferta(offertaB);
        JsonManager.writeBaratto(baratto);
    }

    private Baratto selezionaBaratto(Utente utente, Scambio scambio, View view) {
        List<Baratto> barattoList = JsonManager.readBarattoByUtente(utente.getUsername());
        //se non ci sono offerte
        if (barattoList.size() == 0) {
            view.print("Non sono presenti baratti da accettare");
            return null;
        }

        view.createMenu("Seleziona offerta");
        for (Baratto baratto : barattoList)
            view.addVoceMenu(baratto.getOffertaA().getTitolo() + " \t " + this.calcolaScadenze(baratto, scambio));

        view.addVoceMenu("Esci senza accettare baratti");

        //scelta dell'offerta da accettare
        int scelta = view.scegliVoceMenu();
        //esci
        if (scelta == barattoList.size())
            return null;
        else
            return barattoList.get(scelta);
    }

    private Appuntamento inserisciAppuntamento(Scambio scambio, View view) {
        //mostro luoghi disponibili e scelgo
        view.createMenu("scegli luogo");
        view.setVociMenu((ArrayList<String>) scambio.getLuoghi());
        String luogo = scambio.getLuoghi().get(view.scegliVoceMenu());

        view.print("luogo: " + luogo);

        //mostro giorni disponibili e scelgo
        view.createMenu("scegli giorno");
        ArrayList<String> giorni = new ArrayList<>();
        for (DayOfWeek giorno : scambio.getGiorni()) {
            giorni.add(giorno.name());
        }
        view.setVociMenu(giorni);
        DayOfWeek giorno = DayOfWeek.valueOf(giorni.get(view.scegliVoceMenu()));

        view.print("Giorno: " + giorno.name());

        //mostro orari disponibili e scelgo
        //da mettere a posto
        view.createMenu("scegli orario");
        ArrayList<String> orari = scambio.getOrariScambio();
        view.setVociMenu(orari);
        String orario = orari.get(view.scegliVoceMenu());

        view.print("Orario: " + orario);

        return new Appuntamento(luogo, orario, giorno);
    }

    private String calcolaScadenze(Baratto baratto, Scambio scambio) {
        LocalDateTime oggi = LocalDateTime.now();
        int giorniRisposta = scambio.getScadenzaProposta();
        LocalDateTime scadenza;
        scadenza = baratto.getDataOraBaratto().plusDays(giorniRisposta);
        if (scadenza.isAfter(oggi))
            return String.format("%d giorni rimanenti",scadenza.getDayOfYear() - oggi.getDayOfYear());
        return "scaduta";
    }
}
