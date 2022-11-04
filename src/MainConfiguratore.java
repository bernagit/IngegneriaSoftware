import controller.*;
import controller.Handler;
import model.user.Configuratore;
import view.MyMenu;
import view.View;

public class MainConfiguratore {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        View view = new View();
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
