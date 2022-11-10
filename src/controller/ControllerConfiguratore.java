package controller;

import controller.handlers.LoginConf;
import controller.handlers.Logout;
import model.user.Configuratore;
import view.CliView;
import view.View;

public class ControllerConfiguratore {
    public View view;

    public ControllerConfiguratore(View view) {
        this.view = view;
    }
    public void run(){
        OptionList option = new OptionList();
        String titolo;
        Configuratore configuratore = null;
        do {
            view.createMenu("");
            view.setVociMenu(option.getConfOptionList(configuratore));
            if (configuratore != null) titolo = "Utente "+configuratore.getUsername()+ " loggato";
            else titolo = "Programma Configuratore";
            view.setTitoloMenu(titolo);
            int scelta = view.scegliVoceMenu();
            try {
                Handler handler = option.getOption(scelta).getAction();
                if (handler instanceof LoginConf || handler instanceof Logout)
                    configuratore = (Configuratore) handler.execute(configuratore, view);
                else
                    handler.execute(configuratore, view);
            } catch (ExitException e) {
                view.print(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
