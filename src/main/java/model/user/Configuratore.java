package model.user;

public class Configuratore extends Utente{

    public Configuratore(int id, String username, String password) {
        super(id, username, password);
        this.setUsertype(true);
    }
}
