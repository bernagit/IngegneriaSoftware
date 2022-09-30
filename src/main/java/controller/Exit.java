package controller;

import model.user.Utente;

public class Exit implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        throw new ExitException("Programma Terminato");
    }
}
