package controller;

import model.user.Utente;
import view.View;

public interface Handler {
    Utente execute(Utente utente, View view);
}
