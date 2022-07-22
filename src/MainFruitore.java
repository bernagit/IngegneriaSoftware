import controller.LoginFruit;
import controller.Logout;
import controller.Action;
import controller.ExitException;
import controller.OptionList;
import model.user.Fruitore;
import utility.JsonUtil;
import utility.MyMenu;

public class MainFruitore {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        MyMenu menu = new MyMenu();
        Fruitore fruitore = null;
        String titolo;
        do {
            // eliminazione dei baratti Scaduti
            JsonUtil.eliminaBarattiScaduti();

            menu.setVoci(option.getFruitOptionList(fruitore));
            if (fruitore != null) titolo = "Utente "+fruitore.getUsername()+" loggato";
            else titolo = "Programma fruitore";
            menu.setTitolo(titolo);
            int scelta = menu.scegli();
            try {
                Action action = option.getOption(scelta).getAction();
                if (action instanceof LoginFruit || action instanceof Logout)
                    fruitore = (Fruitore) action.execute(fruitore);
                else
                    action.execute(fruitore);
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
