package controller.handlers;

import controller.Handler;
import model.user.Session;
import model.user.State;
import view.View;

public class Logout implements Handler {
    @Override
    public Session execute(Session session, View view) {
        session.setState(State.UNLOGGED);
        return session;
    }
}
