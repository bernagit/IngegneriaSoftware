package controller;

import model.user.Configuratore;
import utility.DbConnect;
import utility.InputDati;
import model.user.Utente;

public class LoginConf implements Action {
    DbConnect db = new DbConnect();
    private Configuratore doLogin() {
        String user = InputDati.leggiStringaNonVuota("Inserisci username: ");
        String pass = InputDati.leggiStringaNonVuota("Inserisci password: ");
        Utente conf = db.checkLogin(user, pass);
        //controllo correttezza login
        if(conf != null) {
            if (user.equals(conf.getUsername()) && pass.equals(conf.getPassword()) && conf.getUserType()) {
                //se first Login, procedura per il first Login, altrimenti si logga normalmente
                if (conf.getFirstLogin()) {
                    return firstLogin(conf);
                }
                return (Configuratore) conf;
            }
            System.out.println("Login Errato, Profilo Fruitore...");
            return null;
        }
        System.out.println("Login Errato...");
        return null;
    }

    private Configuratore firstLogin(Utente utente) {
        boolean credentialsChanged = false;
        String newUser;
        String newPass;
        do {
            newUser = InputDati.leggiStringaNonVuota("Inserisci nuovo username: ");
            newPass = InputDati.leggiStringaNonVuota("Inserisci nuova password: ");
            String pass = InputDati.leggiStringaNonVuota("Reinserisci nuova password: ");
            if(pass.equals(newPass)) {
                credentialsChanged = db.updateCredentials(utente, newUser, newPass);
                if (!credentialsChanged)
                    System.out.println("Utente già presente, Inseriscine un altro");
            }
            else
                System.out.println("Password non corrispondenti, ripeti registrazione");
        }
        while (!credentialsChanged);
        utente.updateCredentials(newUser, newPass, false);
        System.out.println("Utente " + newUser + " modificato correttamente");
        return (Configuratore) utente;
    }

    @Override
    public Utente execute(Utente utente) throws ExitException {
        return doLogin();
    }
}
