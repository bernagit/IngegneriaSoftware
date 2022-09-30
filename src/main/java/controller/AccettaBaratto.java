package controller;

import model.baratto.Appuntamento;
import model.baratto.Baratto;
import model.offerta.Offerta;
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

public class AccettaBaratto implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.accettaBaratto(utente);
        return null;
    }

    private void accettaBaratto(Utente utente) {
        Scambio scambio = JsonUtil.readScambio();
        //selezione offerta da accettare
        Baratto baratto = selezionaBaratto(utente, scambio);
        if (baratto == null)
            return;

        //chiedo conferma dopo aver mostrato le info dell'offerta
        Offerta offertaBaratto = baratto.getOffertaA();
        System.out.println(offertaBaratto);
        boolean accetta = InputDati.yesOrNo("sei sicuro di voler accettare l'offerta: " + offertaBaratto.getTitolo() + " ? ");


        if (!accetta)
            return;

        baratto.setAppuntamento(this.inserisciAppuntamento(scambio));
        //conferma prima del salvataggio
        boolean save = InputDati.yesOrNo("sei sicuro di voler salvare l'appuntamento? ");
        if (!save) {
            System.out.println("Appuntamento non salvato!");
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
        JsonUtil.writeOfferta(offertaA);
        JsonUtil.writeOfferta(offertaB);
        JsonUtil.writeBaratto(baratto);
    }

    private Baratto selezionaBaratto(Utente utente, Scambio scambio) {
        List<Baratto> barattoList = JsonUtil.readBarattoByUtente(utente.getUsername());
        //se non ci sono offerte
        if (barattoList.size() == 0) {
            System.out.println("Non sono presenti baratti da accettare");
            return null;
        }

        MyMenu menu = new MyMenu("Seleziona offerta");
        for (Baratto baratto : barattoList)
            menu.addVoce(baratto.getOffertaA().getTitolo() + " \t " + this.calcolaScadenze(baratto, scambio));

        menu.addVoce("Esci senza accettare baratti");

        //scelta dell'offerta da accettare
        int scelta = menu.scegli();
        //esci
        if (scelta == barattoList.size())
            return null;
        else
            return barattoList.get(scelta);
    }

    private Appuntamento inserisciAppuntamento(Scambio scambio) {
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
