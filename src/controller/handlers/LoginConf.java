package controller.handlers;

import controller.Handler;
import controller.Session;
import controller.State;
import db.DbConnection;
import model.user.Configuratore;
import model.user.Utente;
import view.View;

public class LoginConf implements Handler {
    @Override
    public Session execute(Session session, View view) {
        return doLogin(session, view);
    }
    DbConnection db = DbConnection.getInstance();
    private Session doLogin(Session session, View view) {
        String user = view.getString("Inserisci username: ");
        String pass = view.getString("Inserisci password: ");
        session.setUtente(db.checkLogin(user, pass));
        //controllo correttezza login
        if(session.getUtente() != null) {
            if (user.equals(session.getUtente().getUsername())
                    && pass.equals(session.getUtente().getPassword())
                    && session.getUtente().getUserType()) {
                //se first Login, procedura per il first Login, altrimenti si logga normalmente
                if (session.getUtente().getFirstLogin()) {
                    return firstLogin(session, view);
                }
                session.setState(State.LOGGED);
                return session;
            }
            view.print("Login Errato, Profilo Fruitore...");
            return session;
        }
        view.print("Login Errato...");
        return session;
    }

    private Session firstLogin(Session session, View view) {
        boolean credentialsChanged = false;
        String newUser;
        String newPass;
        Utente utente = session.getUtente();
        do {
            newUser = view.getString("Inserisci nuovo username: ");
            newPass = view.getString("Inserisci nuova password: ");
            String pass = view.getString("Reinserisci nuova password: ");
            if(pass.equals(newPass)) {
                credentialsChanged = db.updateCredentials(utente, newUser, newPass);
                if (!credentialsChanged)
                    view.print("Utente già presente, Inseriscine un altro");
            }
            else
                view.print("Password non corrispondenti, ripeti registrazione");
        }
        while (!credentialsChanged);
        utente.updateCredentials(newUser, newPass, false);
        view.print("Utente " + newUser + " modificato correttamente");
        session.setUtente(utente);
        session.setState(State.LOGGED);
        return session;
    }
}
