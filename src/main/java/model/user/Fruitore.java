package model.user;

public class Fruitore extends Utente {

    public Fruitore(int id, String username, String password) {
        super(id, username, password);
        this.setUsertype(false);
    }
}
