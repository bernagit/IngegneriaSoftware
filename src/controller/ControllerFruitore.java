package controller;

import controller.handlers.*;
import db.JsonManager;
import model.user.Fruitore;
import view.View;

import java.util.ArrayList;

public class ControllerFruitore {
    final private View view;
    final private ArrayList<Option> options = new ArrayList<>();
    public ControllerFruitore(View view){
        this.view = view;
    }

    public void run(){
        Fruitore fruitore = null;
        String titolo;
        boolean exit = false;
        do {
            // eliminazione dei baratti Scaduti
            JsonManager.eliminaBarattiScaduti();
            view.createMenu("");
            this.setOption(fruitore);
            view.setVociMenu(this.getVoci());
            if (fruitore != null) titolo = "Utente " + fruitore.getUsername() + " loggato";
            else titolo = "Programma fruitore";
            view.setTitoloMenu(titolo);
            int scelta = view.scegliVoceMenu();
            if (scelta != 0) {
                Handler handler = options.get(scelta).getAction();
                if (handler instanceof LoginFruit || handler instanceof Logout)
                    fruitore = (Fruitore) handler.execute(fruitore, view);
                else
                    handler.execute(fruitore, view);
            } else {
                view.print("Programma terminato");
                exit = true;
            }
        } while (!exit);
    }
    private ArrayList<String> getVoci() {
        ArrayList<String> voci = new ArrayList<>();
        for (Option opt: options){
            voci.add(opt.getLabel());
        }
        return voci;
    }
    private void setOption(Fruitore fruitore) {
        options.clear();
        options.add(new Option("Esci", null));
        if (fruitore == null){
            options.add(new Option("Login", new LoginFruit()));
        }
        else {
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
