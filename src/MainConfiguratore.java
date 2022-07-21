import controller.Logout;
import controller.Action;
import controller.ExitException;
import controller.LoginConf;
import controller.OptionList;
import model.user.Configuratore;
import utility.MyMenu;

public class MainConfiguratore {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        MyMenu menu = new MyMenu();
        String titolo;
        Configuratore configuratore = null;
        do {
            menu.setVoci(option.getConfOptionList(configuratore));
            if (configuratore != null) titolo = "Utente "+configuratore.getUsername()+ " loggato";
            else titolo = "Programma Configuratore";
            menu.setTitolo(titolo);
            int scelta = menu.scegli();
            try {
                Action action = option.getOption(scelta).getAction();
                if (action instanceof LoginConf || action instanceof Logout)
                    configuratore = (Configuratore) action.execute(configuratore);
                else
                    action.execute(configuratore);
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
