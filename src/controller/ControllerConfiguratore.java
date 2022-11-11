package controller;

import controller.handlers.*;
import model.user.Configuratore;
import view.View;
import java.util.ArrayList;

public class ControllerConfiguratore {
    final private View view;
    final private ArrayList<Option> options = new ArrayList<>();
    public ControllerConfiguratore(View view) {
        this.view = view;
    }
    public void run(){
        String titolo;
        Configuratore configuratore = null;
        boolean exit = false;
        do {
            view.createMenu("");
            this.setOption(configuratore);
            view.setVociMenu(this.getVoci());
            if (configuratore != null) titolo = "Utente "+configuratore.getUsername()+ " loggato";
            else titolo = "Programma Configuratore";
            view.setTitoloMenu(titolo);
            int scelta = view.scegliVoceMenu();
            try {
                Handler handler = options.get(scelta).getAction();
                if (handler instanceof LoginConf || handler instanceof Logout)
                    configuratore = (Configuratore) handler.execute(configuratore, view);
                else
                    handler.execute(configuratore, view);
            } catch (ExitException e) {
                view.print(e.getMessage());
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

    private void setOption(Configuratore conf){
        options.clear();
        options.add(new Option("Esci", new Exit()));
        if (conf == null){
            options.add(new Option("Login", new LoginConf()));
            options.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
        }
        else {
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
