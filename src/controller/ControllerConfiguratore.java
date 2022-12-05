package controller;

import controller.handlers.*;
import model.user.Session;
import model.user.State;
import view.View;

import java.util.ArrayList;

public class ControllerConfiguratore implements Controller {
    final private View view;
    private Session session;
    final private ArrayList<Option> options;

    public ControllerConfiguratore(View view) {
        this.view = view;
        options = new ArrayList<>();
        session = new Session(null, State.UNLOGGED);
    }

    public void run() {
        String titolo;

        boolean exit = false;
        do {
            view.createMenu("");
            this.setOption();
            view.setVociMenu(this.getVoci());
            if (session.getState().equals(State.LOGGED))
                titolo = "Utente " + session.getUtente().getUsername() + " loggato";
            else
                titolo = "Programma Configuratore";
            view.setTitoloMenu(titolo);
            int scelta = view.scegliVoceMenu();
            if (scelta != 0) {
                //try {
                Handler handler = options.get(scelta).getAction();
                session = handler.execute(session, view);
            } else {
                exit = true;
                view.print("Programma Terminato");
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
            options.add(new Option("Login", new LoginConf()));
            options.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
        } else if (session.getState().equals(State.LOGGED)) {
            options.add(new Option("Logout", new Logout()));
            options.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
            options.add(new Option("Inserisci Gerarchia", new InserisciGerarchia()));
            options.add(new Option("Aggiungi Scambio", new InserisciScambio()));
            options.add(new Option("Visualizza Offerte Aperte", new VisualizzaOfferte()));
            options.add(new Option("Visualizza Offerte Chiuse o In Scambio", new VisualizzaOfferteConfig()));
            options.add(new Option("Importa File Configurazione", new ImportaFileConf()));
        }
    }


}
