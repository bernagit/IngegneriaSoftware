package controller;

import model.user.Utente;
import view.View;

public class Exit implements Handler {
    @Override
    public Utente execute(Utente utente, View view) throws ExitException {
        throw new ExitException("Programma Terminato");
    }
}
