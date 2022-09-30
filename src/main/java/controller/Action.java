package controller;

import model.user.Utente;

public interface Action {
    Utente execute(Utente utente) throws ExitException;
}
