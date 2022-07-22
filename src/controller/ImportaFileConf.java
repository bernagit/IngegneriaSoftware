package controller;

import model.user.Utente;

public class ImportaFileConf implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.importaFile(utente);
        return null;
    }

    private void importaFile(Utente utente) {
        System.out.println("Importa File Configurazione");

    }
}
