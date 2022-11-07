import controller.LoginFruit;
import controller.Logout;
import controller.Handler;
import controller.ExitException;
import controller.OptionList;
import model.user.Fruitore;
import utility.JsonUtil;
import view.View;

public class MainFruitore {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        Fruitore fruitore = null;
        String titolo;
        View view = new View();
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
                System.out.println(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
