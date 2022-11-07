package controller.handlers;

import controller.ExitException;
import controller.Handler;
import model.user.Utente;
import db.JsonUtil;
import view.View;

import java.nio.file.Files;
import java.nio.file.Path;

public class ImportaFileConf implements Handler {
    @Override
    public Utente execute(Utente utente, View view) throws ExitException {
        this.importaFile(view);
        return null;
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
            if (JsonUtil.checkScambioExists()) {
                sovrascrivi = view.getBoolean("Esiste già un file di configurazione, vuoi sovrascriverlo? ");
                if (!sovrascrivi)
                    return;
            }
            boolean result = JsonUtil.scriviFileScambio(path, sovrascrivi);
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
            boolean sovrascrivi = false;
            if (JsonUtil.checkGerarchiaExists(path))
                sovrascrivi = view.getBoolean("Esiste già un file gerarchia con questo nome, vuoi sovrascriverlo? ");
            if (!sovrascrivi) {
                view.print("File Gerarchia non importato");
                return;
            }
            boolean result = JsonUtil.scriviFileGerarchia(path, sovrascrivi);
            if (result)
                view.print("Gerarchia importata con successo");
            else
                view.print("Errore nell'importazione della Gerarchia (file compromesso)");
        } else {
            view.print("Errore nell'importazione della gerarchia (file non trovato o formato non valido)");
        }
    }
}
