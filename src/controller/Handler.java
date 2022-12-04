package controller;

import model.user.Utente;
import view.View;

public interface Handler {
    Session execute(Session session, View view);
}
