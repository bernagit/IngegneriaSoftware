package controller.handlers;

import controller.Handler;
import model.user.Session;
import db.JsonManager;
import model.baratto.Baratto;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Utente;
import view.View;

import java.util.List;

public class VisualizzaOfferteProprietario implements Handler {
    private final JsonManager jsonManager = JsonManager.getInstance();
    @Override
    public Session execute(Session session, View view) {
        this.visualizzaEModifica(session.getUtente(), view);
        return session;
    }

    private void visualizzaEModifica(Utente utente, View view) {
        List<Offerta> offerte = jsonManager.readOffertaByAutore(utente.getUsername());
        view.createMenu("Visualizza, Modifica o Esci");
        if (offerte != null && offerte.size() >= 1) {
            int scelta;
            do {
                //aggiunta voce esci
                view.clearVociMenu();
                view.addVoceMenu("Salva ed Esci");
                for (Offerta offerta : offerte)
                    view.addVoceMenu(offerta.getTitolo() + ": " + offerta.getStatoCorrente());
                //modifica o uscita dal metodo
                scelta = view.scegliVoceMenu();
                if (scelta != 0)
                    this.scegliOpzione(offerte.get(scelta - 1), view);
            } while (scelta != 0);

            for (Offerta offerta : offerte)
                jsonManager.writeOfferta(offerta);

        } else
            view.print("Non sei autore di nessuna offerta.");

    }

    private void scegliOpzione(Offerta offerta, View view) {
        if (offerta.getStatoCorrente().equals(StatoOfferta.APERTA) || offerta.getStatoCorrente().equals(StatoOfferta.RITIRATA))
            this.modificaOfferta(offerta, view);
        else if (offerta.getStatoCorrente().equals(StatoOfferta.IN_SCAMBIO))
            this.visualizzaStato(offerta, view);
        else
            view.print("Impossibile modificare l'offerta in questo stato: "+offerta.getStatoCorrente());
    }

    private void modificaOfferta(Offerta offerta, View view) {
        view.print("Offerta da modificare: " + offerta.getTitolo());
        view.createMenu("Scegli stato Offerta");
        List<StatoOfferta> stati = List.of(StatoOfferta.APERTA, StatoOfferta.RITIRATA);
        for (StatoOfferta stato : stati)
            view.addVoceMenu(stato.name());
        //scelta nuovo stato dal menu
        StatoOfferta newState = stati.get(view.scegliVoceMenu());
        if (!offerta.getStatoCorrente().equals(newState)) {
            offerta.setStatoCorrente(newState);
        }
    }

    private void visualizzaStato(Offerta offerta, View view) {
        Baratto baratto = jsonManager.readBarattobyOfferta(offerta);
        view.print("Offerta in scambio, Ultima risposta ricevuta da: " + baratto.getDecisore());
        view.printAppuntamento(baratto.getAppuntamento());
    }
}

