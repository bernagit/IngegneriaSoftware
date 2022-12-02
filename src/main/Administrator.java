package main;

import db.DbConnection;
import view.CliView;
import view.View;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Administrator {
    static View view = new CliView();
    public static void main(String[] args) {
        DbConnection db = DbConnection.getInstance();
        db.createNewTable("Utenti");

        String password = getNewPassword(10);
        String utente;
        boolean userOk;
        do {
            utente = view.getString("Inserisci nome Utente da creare: ");
            userOk = db.checkUsername(utente);
            if (userOk)
                view.print("Username già presente");
        } while (userOk);
        view.print("password dell'utente " + utente + ": " + password);
        writePasswordToFile(utente, password);
        db.insertUser(utente, password, true, true);
    }

    public static String getNewPassword(int passwordSize) {
        //alphabet contiene tutti i possibili caratteri che comporranno la Password
        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?!<>-*[]{}/";
        int alphabetLength = alphabet.length();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordSize; i++) {
            // Scelgo una delle lettere dell'alfabeto.
            int randomIndexCharInAlphabet = (int) (Math.random() * alphabetLength);
            password.append(alphabet.charAt(randomIndexCharInAlphabet));
        }
        return password.toString();
    }

    public static void writePasswordToFile(String username, String password) {
        try (PrintWriter out = new PrintWriter(username)) {
            out.println(password);
        } catch (FileNotFoundException e) {
            view.print("File non creato");
        }
    }
}