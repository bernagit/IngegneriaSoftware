package controller;

import model.user.Utente;

public class Session {
    private Utente utente;
    private State state;
    public Session(Utente utente, State state) {
        this.utente = utente;
        this.state = state;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    public void setState(State state) {
        this.state = state;
    }
    public Utente getUtente() {
        return utente;
    }
    public State getState() {
        return state;
    }
}

