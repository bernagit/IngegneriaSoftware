package controller;

import controller.handlers.*;
import db.JsonManager;
import view.View;

import java.util.ArrayList;

public class ControllerFruitore implements Controller {
    final private View view;
    private Session session = new Session(null, State.UNLOGGED);
    private JsonManager jsonManager = JsonManager.getInstance();
    final private ArrayList<Option> options = new ArrayList<>();

    public ControllerFruitore(View view) {
        this.view = view;
    }

    public void run() {
        String titolo;
        boolean exit = false;
        do {
            // eliminazione dei baratti Scaduti
            jsonManager.eliminaBarattiScaduti();
            view.createMenu("");
            this.setOption();
            view.setVociMenu(this.getVoci());
            if (session.getState().equals(State.LOGGED))
                titolo = "Utente " + session.getUtente().getUsername() + " loggato";
            else
                titolo = "Programma fruitore";
            view.setTitoloMenu(titolo);
            int scelta = view.scegliVoceMenu();
            if (scelta != 0) {
                Handler handler = options.get(scelta).getAction();
                session = handler.execute(session, view);
            } else {
                view.print("Programma terminato");
                exit = true;
            }
        } while (!exit);
    }

    private ArrayList<String> getVoci() {
        ArrayList<String> voci = new ArrayList<>();
        for (Option opt : options) {
            voci.add(opt.getLabel());
        }
        return voci;
    }

    private void setOption() {
        options.clear();
        options.add(new Option("Esci", null));
        if (session.getState().equals(State.UNLOGGED)) {
            options.add(new Option("Login", new LoginFruit()));
        } else if (session.getState().equals(State.LOGGED)) {
            options.add(new Option("Logout", new Logout()));
            options.add(new Option("Visualizza Scambi", new VisualizzaScambi()));
            options.add(new Option("Pubblica Offerta", new PubblicaOfferta()));
            options.add(new Option("Visualizza Offerte Aperte", new VisualizzaOfferte()));
            options.add(new Option("Visualizza o Modifica Offerte Inserite", new VisualizzaOfferteProprietario()));
            options.add(new Option("Baratta Oggetto", new BarattaOfferta()));
            options.add(new Option("Accetta Baratto", new AccettaBaratto()));
            options.add(new Option("Visualizza e/o Accetta Appuntamenti", new ModificaAppuntamento()));
        }

    }
}
