package controller.handlers;

import controller.Handler;
import model.baratto.Appuntamento;
import model.baratto.Baratto;
import model.offerta.StatoOfferta;
import model.scambio.Scambio;
import model.user.Utente;
import db.JsonManager;
import view.View;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModificaAppuntamento implements Handler {
    private final JsonManager jsonManager = JsonManager.getInstance();
    @Override
    public Utente execute(Utente utente, View view) {
        this.visualizzaAppuntamento(utente, view);
        return null;
    }

    private void visualizzaAppuntamento(Utente utente, View view) {
        Scambio scambio = jsonManager.readScambio();

        List<Baratto> barattoList = jsonManager.readBarattoInScambio(utente.getUsername());
        if ( barattoList != null && barattoList.size() == 0) {
            view.print("Non sono presenti Appuntamenti per le offerte inserite");
            return;
        }

        view.createMenu("Scegli Appuntamento");
        ArrayList<String> voci = new ArrayList<>();
        StringBuilder voce = new StringBuilder();
        for (Baratto baratto : barattoList) {
            voce.append(baratto.getOffertaA().getTitolo()).append(" per: ").append(baratto.getOffertaB().getTitolo())
                    .append("\t (").append(this.calcolaScadenze(baratto, scambio))
                    .append("\tultima risposta da: ").append(baratto.getDecisore())
                    .append(")");
            voci.add(voce.toString());
        }
        voci.add("Torna al menu");
        view.setVociMenu(voci);

        int scelta = view.scegliVoceMenu();
        if (scelta == voci.size() - 1) {
            return;
        }
        //rispondi all'offerta
        this.accettaORispondi(barattoList.get(scelta), utente, view);
    }

    private void accettaORispondi(Baratto baratto, Utente utente, View view) {
        if (baratto.getDecisore() != null && baratto.getDecisore().equals(utente.getUsername())) {
            view.print("l'appuntamento deve essere confermato dall'altro utente");
            return;
        }
        view.print("\nDettagli appuntamento:");
        view.printAppuntamento(baratto.getAppuntamento());

        boolean accetta = view.getBoolean("Vuoi accettare l'appuntamento? ");
        if (accetta)
            this.accettaBaratto(baratto);
        else{
            boolean modifica = view.getBoolean("Vuoi modificare l'appuntamento? ");
            if(modifica)
                this.nuovoAppuntamento(baratto, view);
        }


    }

    private void nuovoAppuntamento(Baratto baratto, View view) {
        Scambio scambio = jsonManager.readScambio();
        Appuntamento appuntamento = this.cambiaAppuntamento(scambio, baratto, view);
        if (appuntamento == null) {
            view.print("Appuntamento inserito uguale a quello deciso dall'altro utente");
            boolean accetta = view.getBoolean("Vuoi accettare dunque? ");
            if (accetta)
                this.accettaBaratto(baratto);
            return;
        }

        baratto.setAppuntamento(appuntamento);
        //cambio risposta utente
        if (baratto.getDecisore().equals(baratto.getUtenteA())) {
            baratto.setDecisore(baratto.getUtenteB());
        } else {
            baratto.setDecisore(baratto.getUtenteA());
        }

        //scrivo baratto
        jsonManager.writeBaratto(baratto);
    }

    private void accettaBaratto(Baratto baratto) {
        baratto.getOffertaA().setStatoCorrente(StatoOfferta.CHIUSA);
        baratto.getOffertaB().setStatoCorrente(StatoOfferta.CHIUSA);
        jsonManager.writeOfferta(baratto.getOffertaA());
        jsonManager.writeOfferta(baratto.getOffertaB());
        //elimino baratto
        jsonManager.deleteBaratto(baratto);
    }

    private Appuntamento cambiaAppuntamento(Scambio scambio, Baratto baratto, View view) {
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

        view.print("Giorno: " + giorno);

        //mostro orari disponibili e scelgo
        view.createMenu("scegli orario");
        ArrayList<String> orari = scambio.getOrariScambio();
        view.setVociMenu(orari);
        String orario = orari.get(view.scegliVoceMenu());

        view.print("Orario: " + orario);

        Appuntamento nuovoAppuntamento = new Appuntamento(luogo, orario, giorno);

        if (nuovoAppuntamento.equals(baratto.getAppuntamento()))
            return null;
        else
            return nuovoAppuntamento;
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
