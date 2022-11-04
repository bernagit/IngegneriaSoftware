import controller.LoginFruit;
import controller.Logout;
import controller.Handler;
import controller.ExitException;
import controller.OptionList;
import model.user.Fruitore;
import utility.JsonUtil;
import view.MyMenu;
import view.View;

public class MainFruitore {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        MyMenu menu = new MyMenu("");
        Fruitore fruitore = null;
        String titolo;
        View genericView = null;
        do {
            // eliminazione dei baratti Scaduti
            JsonUtil.eliminaBarattiScaduti();

            menu.setVoci(option.getFruitOptionList(fruitore));
            if (fruitore != null) titolo = "Utente "+fruitore.getUsername()+" loggato";
            else titolo = "Programma fruitore";
            menu.setTitolo(titolo);
            int scelta = menu.scegli();
            try {
                Handler handler = option.getOption(scelta).getAction();
                if (handler instanceof LoginFruit || handler instanceof Logout)
                    fruitore = (Fruitore) handler.execute(fruitore, genericView);
                else
                    handler.execute(fruitore, genericView);
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
