package controller.handlers;

import controller.ExitException;
import controller.Handler;
import model.baratto.Baratto;
import model.gerarchia.Categoria;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Utente;
import db.JsonUtil;
import view.View;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BarattaOfferta implements Handler {
    @Override
    public Utente execute(Utente utente, View view) throws ExitException {
        this.barattaOfferta(utente, view);
        return null;
    }

    private void barattaOfferta(Utente utente, View view) {
        List<Offerta> offerteAperte = JsonUtil.readOffertaByAutoreAndState(utente.getUsername(), StatoOfferta.APERTA);
        view.createMenu("Scegli oggetto da barattare");
        if (offerteAperte.size() < 1) {
            view.print("Non sono presenti Offerte nello stato Aperto");
            return;
        }
        //creazione menu
        for (Offerta offerta : offerteAperte) {
            view.addVoceMenu(offerta.getTitolo());
        }
        view.addVoceMenu("Esci senza barattare");
        //scelta dell'offerta da barattare
        int scelta = view.scegliVoceMenu();
        //esci
        if (scelta == offerteAperte.size())
            return;

        Offerta offertaDaBarattare = offerteAperte.get(scelta);
        Offerta offertaScelta = this.scegliOffertaAltroAutore(utente, offertaDaBarattare.getCategoria(), view);
        if (offertaScelta == null)
            return;
        view.print("Dettagli offerta:\n");
        view.printOfferta(offertaScelta);
        boolean conferma = view.getBoolean("Sei sicuro di voler scambiare " + offertaDaBarattare.getTitolo()
                + " con: " + offertaScelta.getTitolo());
        if (!conferma) {
            return;
        }
        view.print("Baratto avviato");
        Baratto baratto = this.inserisciBaratto(offertaDaBarattare, offertaScelta);
        baratto.setDecisore(offertaDaBarattare.getAutore());
        JsonUtil.writeBaratto(baratto);
    }

    private Offerta scegliOffertaAltroAutore(Utente utente, Categoria categoria, View view) {
        List<Offerta> offerte = JsonUtil.readOfferteApertebyCategoria(utente.getUsername(), categoria);
        view.createMenu("Scegli oggetto che vorresti");
        if (offerte.size() < 1) {
            view.print("Non sono presenti offerte aperte della stessa categoria");
        } else {
            for (Offerta offerta : offerte) {
                view.addVoceMenu(offerta.getTitolo());
            }
            view.addVoceMenu("Esci senza barattare");
            int scelta = view.scegliVoceMenu();
            if (scelta == offerte.size())
                return null;
            return offerte.get(scelta);
        }
        return null;
    }

    private Baratto inserisciBaratto(Offerta offertaDaBarattare, Offerta offertaScelta) {
        //cambio di stato delle offerte
        offertaDaBarattare.setStatoCorrente(StatoOfferta.ACCOPPIATA);
        JsonUtil.writeOfferta(offertaDaBarattare);
        offertaScelta.setStatoCorrente(StatoOfferta.SELEZIONATA);
        JsonUtil.writeOfferta(offertaScelta);
        //creo baratto
        return new Baratto(offertaDaBarattare, offertaScelta, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString());
    }
}
