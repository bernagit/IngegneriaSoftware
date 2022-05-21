package controller;

import model.baratto.Baratto;
import model.gerarchia.Categoria;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;
import utility.MyMenu;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BarattaOfferta implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.barattaOfferta(utente);
        return null;
    }

    private void barattaOfferta(Utente utente) {
        List<Offerta> offerteAperte = JsonUtil.readOffertaByAutoreAndState(utente.getUsername(), StatoOfferta.APERTA);
        MyMenu menu = new MyMenu("Scegli oggetto da barattare");
        if (offerteAperte.size() < 1) {
            System.out.println("Non sono presenti Offerte nello stato Aperto");
            return;
        }
        //creazione menu
        for (Offerta offerta : offerteAperte) {
            menu.addVoce(offerta.getTitolo());
        }
        menu.addVoce("Esci senza barattare");
        //scelta dell'offerta da barattare
        int scelta = menu.scegli();
        //esci
        if (scelta == offerteAperte.size())
            return;

        Offerta offertaDaBarattare = offerteAperte.get(scelta);
        Offerta offertaScelta = this.scegliOffertaAltroAutore(utente, offertaDaBarattare.getCategoria());
        if (offertaScelta == null)
            return;
        System.out.println("Dettagli offerta:\n" + offertaScelta);
        boolean conferma = InputDati.yesOrNo("Sei sicuro di voler scambiare " + offertaDaBarattare.getTitolo()
                + " con: " + offertaScelta.getTitolo());
        if (!conferma) {
            return;
        }
        System.out.println("Baratto avviato");
        Baratto baratto = this.inserisciBaratto(offertaDaBarattare, offertaScelta);
        JsonUtil.writeBaratto(baratto);
    }

    private Offerta scegliOffertaAltroAutore(Utente utente, Categoria categoria) {
        List<Offerta> offerte = JsonUtil.readOfferteApertebyCategoria(utente.getUsername(), categoria);
        MyMenu menu = new MyMenu("Scegli oggetto che vorresti");
        if (offerte.size() < 1) {
            System.out.println("Non sono presenti offerte aperte della stessa categoria");
        } else {
            for (Offerta offerta : offerte) {
                menu.addVoce(offerta.getTitolo());
            }
            menu.addVoce("Esci senza barattare");
            int scelta = menu.scegli();
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
        return new Baratto(offertaDaBarattare, offertaScelta, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
    }
}
