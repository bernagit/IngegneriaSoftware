package controller.handlers;

import controller.Handler;
import model.user.Fruitore;
import utility.DbConnect;
import model.user.Utente;
import view.View;

public class LoginFruit implements Handler {

    DbConnect db = new DbConnect();
    @Override
    public Utente execute(Utente utente, View view) {
        return doLogin(view);
    }

    private Utente doLogin(View view) {
        boolean firstLogin = view.getBoolean("Primo Login? ");
        if (firstLogin)
            return this.firstLogin(view);
        else
            return this.normalLogin(view);
    }

    private Fruitore normalLogin(View view) {

        String user = view.getString("Inserisci username: ");
        String pass = view.getString("Inserisci password: ");
        Utente fruitore = db.checkLogin(user, pass);
        if (fruitore != null) {
            if (user.equals(fruitore.getUsername()) && pass.equals(fruitore.getPassword()) && !fruitore.getUserType())
                return (Fruitore) fruitore;
            else {
                view.print("Login Errato, profilo Configuratore...");
                return null;
            }
        }
        else view.print("Login Errato, credenziali non valide");
        return null;
    }

    private Fruitore firstLogin(View view) {
        boolean userOk;
        String user;
        String password;
        do {
            user = view.getString("Inserisci username: ");
            userOk = db.checkNewUser(user);
            if (!userOk)
                view.print("Username già presente");
        } while (!userOk);
        boolean passOk = false;
        do {
            password = view.getString("Inserisci password: ");
            String ripeti = view.getString("Ripeti password: ");
            if (password.equals(ripeti))
                passOk = true;
            else
                view.print("Passowrd diverse...");
        } while (!passOk);
        return (Fruitore) db.insertUser(user, password, false, false);

    }
}
