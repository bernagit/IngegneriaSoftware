package controller;

import model.user.Session;
import view.View;

public interface Handler {
    Session execute(Session session, View view);
}
