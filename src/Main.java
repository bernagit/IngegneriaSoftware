import controller.Logout;
import controller.ExitException;
import controller.Login;
import model.OptionList;
import utility.MyMenu;

public class Main {
    public static void main(String[] args) {
        OptionList option = new OptionList();
        MyMenu menu = new MyMenu("Programma Configuratore");
        boolean logged = false;

        do{
            if(!logged){
                menu.setVoci(option.getOptionList(false));
            }
            else{
                menu.setVoci(option.getOptionList(true));
            }
            int scelta = menu.scegli();
            try {
                if (option.getOption(scelta).getAction() instanceof Login || option.getOption(scelta).getAction() instanceof Logout) {
                    logged = option.getOption(scelta).getAction().execute();
                }
                else {
                    option.getOption(scelta).getAction().execute();
                }
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }while(true);
    }
}
