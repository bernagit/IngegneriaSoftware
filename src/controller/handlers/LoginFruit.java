package controller.handlers;

import controller.Handler;
import model.user.Session;
import model.user.State;
import db.DbConnection;
import model.user.Fruitore;
import model.user.Utente;
import view.View;

public class LoginFruit implements Handler {

    DbConnection db = DbConnection.getInstance();
    @Override
    public Session execute(Session session, View view) {
        return doLogin(session, view);
    }

    private Session doLogin(Session session, View view) {
        boolean firstLogin = view.getBoolean("Primo Login? ");
        if (firstLogin)
            return this.firstLogin(session, view);
        else
            return this.normalLogin(session, view);
    }

    private Session normalLogin(Session session, View view) {
        String user = view.getString("Inserisci username: ");
        String pass = view.getString("Inserisci password: ");
        Utente fruitore = db.checkLogin(user, pass);
        if (fruitore != null) {
            if (user.equals(fruitore.getUsername()) && pass.equals(fruitore.getPassword()) && !fruitore.getUserType()){
                session.setUtente(fruitore);
                session.setState(State.LOGGED);
            }
            else {
                view.print("Login Errato, profilo Configuratore...");
            }
            return session;
        }
        else view.print("Login Errato, credenziali non valide");
        return session;
    }

    private Session firstLogin(Session session, View view) {
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
        Fruitore fruitore = (Fruitore) db.insertUser(user, password, false, false);
        session.setState(State.LOGGED);
        session.setUtente(fruitore);
        return session;
    }
}
