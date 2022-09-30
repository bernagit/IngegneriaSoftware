package controller;

import model.baratto.Appuntamento;
import model.baratto.Baratto;
import model.offerta.StatoOfferta;
import model.scambio.Scambio;
import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;
import utility.MyMenu;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModificaAppuntamento implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.visualizzaAppuntamento(utente);
        return null;
    }

    private void visualizzaAppuntamento(Utente utente) {
        Scambio scambio = JsonUtil.readScambio();

        List<Baratto> barattoList = JsonUtil.readBarattoInScambio(utente.getUsername());
        if ( barattoList != null && barattoList.size() == 0) {
            System.out.println("Non sono presenti Appuntamenti per le offerte inserite");
            return;
        }

        MyMenu menu = new MyMenu("Scegli Appuntamento");
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
        menu.setVoci(voci);

        int scelta = menu.scegli();
        if (scelta == voci.size() - 1) {
            return;
        }
        //rispondi all'offerta
        this.accettaORispondi(barattoList.get(scelta), utente);
    }

    private void accettaORispondi(Baratto baratto, Utente utente) {
        if (baratto.getDecisore() != null && baratto.getDecisore().equals(utente.getUsername())) {
            System.out.println("l'appuntamento deve essere confermato dall'altro utente");
            return;
        }
        System.out.println("\nDettagli appuntamento:" + baratto.getAppuntamento());



        boolean accetta = InputDati.yesOrNo("Vuoi accettare l'appuntamento? ");
        if (accetta)
            this.accettaBaratto(baratto);
        else{
            boolean modifica = InputDati.yesOrNo("Vuoi modificare l'appuntamento? ");
            if(modifica)
                this.nuovoAppuntamento(baratto, utente);
        }


    }

    private void nuovoAppuntamento(Baratto baratto, Utente utente) {
        Scambio scambio = JsonUtil.readScambio();
        Appuntamento appuntamento = this.cambiaAppuntamento(scambio, baratto);
        if (appuntamento == null) {
            System.out.println("Appuntamento inserito uguale a quello deciso dall'altro utente");
            boolean accetta = InputDati.yesOrNo("Vuoi accettare dunque? ");
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
        JsonUtil.writeBaratto(baratto);
    }

    private void accettaBaratto(Baratto baratto) {
        baratto.getOffertaA().setStatoCorrente(StatoOfferta.CHIUSA);
        baratto.getOffertaB().setStatoCorrente(StatoOfferta.CHIUSA);
        JsonUtil.writeOfferta(baratto.getOffertaA());
        JsonUtil.writeOfferta(baratto.getOffertaB());
        //elimino baratto
        JsonUtil.deleteBaratto(baratto);
    }

    private Appuntamento cambiaAppuntamento(Scambio scambio, Baratto baratto) {
        //mostro luoghi disponibili e scelgo
        MyMenu menuLuoghi = new MyMenu("scegli luogo");
        menuLuoghi.setVoci((ArrayList<String>) scambio.getLuoghi());
        String luogo = scambio.getLuoghi().get(menuLuoghi.scegli());

        System.out.println("luogo: " + luogo);

        //mostro giorni disponibili e scelgo
        MyMenu menuGiorni = new MyMenu("scegli giorno");
        ArrayList<String> giorni = new ArrayList<>();
        for (DayOfWeek giorno : scambio.getGiorni()) {
            giorni.add(giorno.name());
        }
        menuGiorni.setVoci(giorni);
        DayOfWeek giorno = DayOfWeek.valueOf(giorni.get(menuGiorni.scegli()));

        System.out.println("Giorno: " + giorno);

        //mostro orari disponibili e scelgo
        MyMenu menuOrari = new MyMenu("scegli orario");
        ArrayList<String> orari = scambio.getOrariScambio();
        menuOrari.setVoci(orari);
        String orario = orari.get(menuOrari.scegli());

        System.out.println("Orario: " + orario);

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
