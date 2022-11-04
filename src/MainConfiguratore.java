import controller.*;
import controller.Handler;
import model.user.Configuratore;
import view.MyMenu;
import view.View;

public class MainConfiguratore {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        View genericView = null;
        MyMenu menu = new MyMenu("");
        String titolo;
        Configuratore configuratore = null;
        do {
            menu.setVoci(option.getConfOptionList(configuratore));
            if (configuratore != null) titolo = "Utente "+configuratore.getUsername()+ " loggato";
            else titolo = "Programma Configuratore";
            menu.setTitolo(titolo);
            int scelta = menu.scegli();
            try {
                Handler handler = option.getOption(scelta).getAction();
                if (handler instanceof LoginConf || handler instanceof Logout)
                    configuratore = (Configuratore) handler.execute(configuratore, genericView);
                else
                    handler.execute(configuratore, genericView);
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        } while (true);
    }
}
