package model.user;

public abstract class Utente {
    private final int id;
    private String username;
    private String password;
    private boolean firstLogin;

    public Utente(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public void setFirstLogin(boolean firstLogin){
        this.firstLogin = firstLogin;
    }
    public boolean getFirstLogin(){
        return this.firstLogin;
    }
}
