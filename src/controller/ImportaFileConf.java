package controller;

import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;

import java.nio.file.Files;
import java.nio.file.Path;

public class ImportaFileConf implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.importaFile();
        return null;
    }

    private void importaFile() {
        String scelta;
        boolean sceltaOk = false;
        do {
            scelta = InputDati.leggiStringaNonVuota(
                    "Vuoi importare una gerarchia o i valori dei parametri di configurazione? (ger/conf): ");
            if (scelta.equals("ger")) {
                sceltaOk = true;
                this.importaGerarchia();
            } else if (scelta.equals("conf")) {
                sceltaOk = true;
                this.importaConfigurazione();
            } else {
                System.out.println("Scelta non valida, (ger/conf uniche scelte possibili)");
            }
        } while (!sceltaOk);
    }

    private void importaConfigurazione() {
        String strPath = InputDati.leggiStringaNonVuota("Inserisci il percorso del file di configurazione: ");
        Path path = Path.of(strPath);
        if (strPath.endsWith(".json") && Files.exists(path)) {
            boolean sovrascrivi = false;
            if (JsonUtil.checkScambioExists()) {
                sovrascrivi = InputDati.yesOrNo("Esiste già un file di configurazione, vuoi sovrascriverlo? ");
                if (!sovrascrivi)
                    return;
            }
            boolean result = JsonUtil.scriviFileScambio(path, sovrascrivi);
            if (result)
                System.out.println("File di configurazione importato con successo");
            else
                System.out.println("Errore nell'importazione del file di configurazione (file compromesso)");
        } else {
            System.out.println("Errore nell'importazione della configurazione (file non trovato o formato non valido)");
        }
    }

    private void importaGerarchia() {
        String strPath = InputDati.leggiStringaNonVuota("Inserisci il percorso del file Gerarchia: ");
        Path path = Path.of(strPath);
        if (strPath.endsWith(".json") && Files.exists(path)) {
            boolean sovrascrivi = false;
            if (JsonUtil.checkGerarchiaExists(path))
                sovrascrivi = InputDati.yesOrNo("Esiste già un file gerarchia con questo nome, vuoi sovrascriverlo? ");
            if (!sovrascrivi) {
                System.out.println("File Gerarchia non importato");
                return;
            }
            boolean result = JsonUtil.scriviFileGerarchia(path, sovrascrivi);
            if (result)
                System.out.println("Gerarchia importata con successo");
            else
                System.out.println("Errore nell'importazione della Gerarchia (file compromesso)");

        } else {
            System.out.println("Errore nell'importazione della gerarchia (file non trovato o formato non valido)");
        }
    }
}
