package controller;

import controller.handlers.LoginFruit;
import controller.handlers.Logout;
import model.user.Fruitore;
import db.JsonUtil;
import view.View;

public class ControllerFruitore {
    private View view;

    public ControllerFruitore(View view){
        this.view = view;
    }

    public void run(){
        OptionList option = new OptionList();
        Fruitore fruitore = null;
        String titolo;
        do {
            // eliminazione dei baratti Scaduti
            JsonUtil.eliminaBarattiScaduti();
            view.createMenu("");
            view.setVociMenu(option.getFruitOptionList(fruitore));
            if (fruitore != null) titolo = "Utente "+fruitore.getUsername()+" loggato";
            else titolo = "Programma fruitore";
            view.setTitoloMenu(titolo);
            int scelta = view.scegliVoceMenu();
            try {
                Handler handler = option.getOption(scelta).getAction();
                if (handler instanceof LoginFruit || handler instanceof Logout)
                    fruitore = (Fruitore) handler.execute(fruitore, view);
                else
                    handler.execute(fruitore, view);
            } catch (ExitException e) {
                view.print(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
