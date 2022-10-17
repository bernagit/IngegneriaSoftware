package controller;

import model.baratto.Baratto;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Utente;
import utility.JsonUtil;
import utility.MyMenu;

import java.util.List;

public class VisualizzaOfferteProprietario implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.visualizzaEModifica(utente);
        return null;
    }

    private void visualizzaEModifica(Utente utente) {
        List<Offerta> offerte = JsonUtil.readOffertaByAutore(utente.getUsername());
        MyMenu menu = new MyMenu("Visualizza, Modifica o Esci");
        if (offerte != null && offerte.size() >= 1) {
            int scelta;
            do {
                //aggiunta voce esci
                menu.clearVoci();
                menu.addVoce("Salva ed Esci");
                for (Offerta offerta : offerte)
                    menu.addVoce(offerta.getTitolo() + ": " + offerta.getStatoCorrente());
                //modifica o uscita dal metodo
                scelta = menu.scegli();
                if (scelta != 0)
                    this.scegliOpzione(offerte.get(scelta - 1));
            } while (scelta != 0);

            for (Offerta offerta : offerte)
                JsonUtil.writeOfferta(offerta);

        } else
            System.out.println("Non sei autore di nessuna offerta.");

    }

    private void scegliOpzione(Offerta offerta) {
        if (offerta.getStatoCorrente().equals(StatoOfferta.APERTA) || offerta.getStatoCorrente().equals(StatoOfferta.RITIRATA))
            this.modificaOfferta(offerta);
        else if (offerta.getStatoCorrente().equals(StatoOfferta.IN_SCAMBIO))
            this.visualizzaStato(offerta);
        else
            System.out.println("Impossibile modificare l'offerta in questo stato: "+offerta.getStatoCorrente());
    }

    public void modificaOfferta(Offerta offerta) {
        System.out.println("Offerta da modificare: " + offerta.getTitolo());
        MyMenu menu = new MyMenu("Scegli stato Offerta");
        List<StatoOfferta> stati = List.of(StatoOfferta.APERTA, StatoOfferta.RITIRATA);
        for (StatoOfferta stato : stati)
            menu.addVoce(stato.name());
        //scelta nuovo stato dal menu
        StatoOfferta newState = stati.get(menu.scegli());
        if (!offerta.getStatoCorrente().equals(newState)) {
            offerta.setStatoCorrente(newState);
        }
    }

    private void visualizzaStato(Offerta offerta) {
        Baratto baratto = JsonUtil.readBarattobyOfferta(offerta);
        System.out.println("Offerta in scambio, Ultima risposta ricevuta da: " + baratto.getDecisore());
        System.out.println(baratto.getAppuntamento());
    }
}

