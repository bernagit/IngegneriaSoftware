package model.user;

public abstract class Utente {
    private int id;
    private String username;
    private String password;
    private boolean firstLogin;
    private boolean usertype;

    public Utente(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean getFirstLogin() {
        return this.firstLogin;
    }

    public boolean getUserType() {
        return this.usertype;
    }

    public void setUsertype(boolean usertype) {
        this.usertype = usertype;
    }

    public void updateCredentials(String newUser, String newPass, boolean firstLogin) {
        this.username = newUser;
        this.password = newPass;
        this.firstLogin = firstLogin;
    }
}
