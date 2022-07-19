package controller;

import model.user.Utente;

public interface Action {
    boolean execute() throws ExitException;
}
