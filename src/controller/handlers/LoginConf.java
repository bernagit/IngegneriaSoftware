package controller.handlers;

import controller.Handler;
import model.user.Configuratore;
import db.DbConnect;
import model.user.Utente;
import view.View;

public class LoginConf implements Handler {
    @Override
    public Utente execute(Utente utente, View view) {
        return doLogin(view);
    }
    DbConnect db = new DbConnect();
    private Configuratore doLogin(View view) {
        String user = view.getString("Inserisci username: ");
        String pass = view.getString("Inserisci password: ");
        Utente conf = db.checkLogin(user, pass);
        //controllo correttezza login
        if(conf != null) {
            if (user.equals(conf.getUsername()) && pass.equals(conf.getPassword()) && conf.getUserType()) {
                //se first Login, procedura per il first Login, altrimenti si logga normalmente
                if (conf.getFirstLogin()) {
                    return firstLogin(conf, view);
                }
                return (Configuratore) conf;
            }
            view.print("Login Errato, Profilo Fruitore...");
            return null;
        }
        view.print("Login Errato...");
        return null;
    }

    private Configuratore firstLogin(Utente utente, View view) {
        boolean credentialsChanged = false;
        String newUser;
        String newPass;
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
        return (Configuratore) utente;
    }
}
