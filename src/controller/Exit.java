package controller;

public class Exit implements Action {
    @Override
    public boolean execute() throws ExitException {
        throw new ExitException("Programma Terminato");
    }
}
