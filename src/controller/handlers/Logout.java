package controller.handlers;

import controller.Handler;
import controller.Session;
import controller.State;
import model.user.Utente;
import view.View;

public class Logout implements Handler {
    @Override
    public Session execute(Session session, View view) {
        session.setState(State.UNLOGGED);
        return session;
    }
}
