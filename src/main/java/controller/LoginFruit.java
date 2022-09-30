package controller;

import model.user.Fruitore;
import utility.DbConnect;
import utility.InputDati;
import model.user.Utente;

public class LoginFruit implements Action {

    DbConnect db = new DbConnect();

    @Override
    public Utente execute(Utente utente) {
        return doLogin();
    }

    private Utente doLogin() {
        boolean firstLogin = InputDati.yesOrNo("Primo Login? ");
        if (firstLogin)
            return this.firstLogin();
        else
            return this.normalLogin();
    }

    private Fruitore normalLogin() {

        String user = InputDati.leggiStringaNonVuota("Inserisci username: ");
        String pass = InputDati.leggiStringaNonVuota("Inserisci password: ");
        Utente fruitore = db.checkLogin(user, pass);
        if (fruitore != null) {
            if (user.equals(fruitore.getUsername()) && pass.equals(fruitore.getPassword()) && !fruitore.getUserType())
                return (Fruitore) fruitore;
            else {
                System.out.println("Login Errato, profilo Configuratore...");
                return null;
            }
        }
        else System.out.println("Login Errato, credenziali non valide");
        return null;
    }

    private Fruitore firstLogin() {
        boolean userOk;
        String user;
        String password;
        do {
            user = InputDati.leggiStringaNonVuota("Inserisci username: ");
            userOk = db.checkNewUser(user);
            if (!userOk)
                System.out.println("Username già presente");
            else
                userOk = true;
        } while (!userOk);
        boolean passOk = false;
        do {
            password = InputDati.leggiStringaNonVuota("Inserisci password: ");
            String ripeti = InputDati.leggiStringaNonVuota("Ripeti password: ");
            if (password.equals(ripeti))
                passOk = true;
            else
                System.out.println("Passowrd diverse...");
        } while (!passOk);
        return (Fruitore) db.insertUser(user, password, false, false);

    }
}
