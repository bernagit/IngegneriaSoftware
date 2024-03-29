package controller.handlers;

import controller.Handler;
import model.user.Session;
import db.JsonManager;
import view.View;

import java.nio.file.Files;
import java.nio.file.Path;

public class ImportaFileConf implements Handler {
    private final JsonManager jsonManager = JsonManager.getInstance();
    @Override
    public Session execute(Session session, View view) {
        this.importaFile(view);
        return session;
    }

    private void importaFile(View view) {
        String scelta;
        boolean sceltaOk = false;
        do {
            scelta = view.getString("Vuoi importare una gerarchia o i valori dei parametri di configurazione? (ger/conf): ");
            if (scelta.equals("ger")) {
                sceltaOk = true;
                this.importaGerarchia(view);
            } else if (scelta.equals("conf")) {
                sceltaOk = true;
                this.importaConfigurazione(view);
            } else {
                view.print("Scelta non valida, (ger/conf uniche scelte possibili)");
            }
        } while (!sceltaOk);
    }

    private void importaConfigurazione(View view) {
        String strPath = view.getString("Inserisci il percorso del file di configurazione: ");
        Path path = Path.of(strPath);
        if (strPath.endsWith(".json") && Files.exists(path)) {
            boolean sovrascrivi = false;
            if (jsonManager.checkScambioExists()) {
                sovrascrivi = view.getBoolean("Esiste già un file di configurazione, vuoi sovrascriverlo? ");
                if (!sovrascrivi)
                    return;
            }
            boolean result = jsonManager.scriviFileScambio(path, sovrascrivi);
            if (result)
                view.print("File di configurazione importato con successo");
            else
                view.print("Errore nell'importazione del file di configurazione (file compromesso)");
        } else {
            view.print("Errore nell'importazione della configurazione (file non trovato o formato non valido)");
        }
    }

    private void importaGerarchia(View view) {
        String strPath = view.getString("Inserisci il percorso del file Gerarchia: ");
        Path path = Path.of(strPath);
        if (strPath.endsWith(".json") && Files.exists(path)) {
            boolean sovrascrivi = true;
            if (jsonManager.checkGerarchiaExists(path))
                sovrascrivi = view.getBoolean("Esiste già un file gerarchia con questo nome, vuoi sovrascriverlo? ");
            if (!sovrascrivi) {
                view.print("File Gerarchia non importato");
                return;
            }
            boolean result = jsonManager.scriviFileGerarchia(path, sovrascrivi);
            if (result)
                view.print("Gerarchia importata con successo");
            else
                view.print("Errore nell'importazione della Gerarchia (file compromesso)");
        } else {
            view.print("Errore nell'importazione della gerarchia (file non trovato o formato non valido)");
        }
    }
}
