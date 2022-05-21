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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ModificaAppuntamento implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.visualizzaAppuntamento(utente);
        return null;
    }

    private void visualizzaAppuntamento(Utente utente) {
        List<Baratto> barattoList = JsonUtil.readBarattoInScambio(utente.getUsername());
        if (barattoList.size() == 0) {
            System.out.println("Non sono presenti Appuntamenti per le offerte inserite");
            return;
        }

        MyMenu menu = new MyMenu("Scegli offerta");
        ArrayList<String> voci = new ArrayList<>();
        StringBuilder voce = new StringBuilder();
        for (Baratto baratto : barattoList) {
            voce.append(baratto.getOffertaA().getTitolo()).append(" per: ").append(baratto.getOffertaB().getTitolo());
            voci.add(voce.toString());
        }

        menu.setVoci(voci);
        //rispondi all'offerta
        this.accettaORispondi(barattoList.get(menu.scegli()), utente);
    }

    private void accettaORispondi(Baratto baratto, Utente utente) {
        if (baratto.getDecisore() != null && !baratto.getDecisore().equals(utente.getUsername())) {
            System.out.println("l'appuntamento deve essere confermato dall'altro utente");
            return;
        }
        System.out.println("\nDettagli appuntamento:" + baratto.getAppuntamento());
        Boolean accetta = InputDati.yesOrNo("Vuoi accettare l'appuntamento? ");
        if (accetta)
            this.accettaBaratto(baratto, utente);
        else
            this.nuovoAppuntamento(baratto, utente);

    }

    private void nuovoAppuntamento(Baratto baratto, Utente utente) {
        Scambio scambio = JsonUtil.readScambio();
        Appuntamento appuntamento = this.cambiaAppuntamento(scambio, baratto);
        if (appuntamento == null) {
            System.out.println("Appuntamento inserito uguale a quello deciso dall'altro utente");
            boolean accetta = InputDati.yesOrNo("Vuoi accettare dunque? ");
            if (accetta)
                this.accettaBaratto(baratto, utente);
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

    private void accettaBaratto(Baratto baratto, Utente utente) {
        baratto.getOffertaA().setStatoCorrente(StatoOfferta.CHIUSA);
        baratto.getOffertaB().setStatoCorrente(StatoOfferta.CHIUSA);
        JsonUtil.writeOfferta(baratto.getOffertaA());
        JsonUtil.writeOfferta(baratto.getOffertaB());
        //elimino il baratto ??
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
        //da mettere a posto
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
}
