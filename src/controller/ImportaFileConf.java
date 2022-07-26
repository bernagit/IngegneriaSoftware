package controller;

import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
        }while (!sceltaOk);
    }

    private void importaConfigurazione() {
        String strPath = InputDati.leggiStringaNonVuota("Inserisci il percorso del file di configurazione: ");
        Path path = Path.of(strPath);
        if (strPath.endsWith(".json") && Files.exists(path)) {
            if(JsonUtil.checkScambioExists()){
                boolean sovrascrivi = InputDati.yesOrNo("Esiste già un file di configurazione, vuoi sovrascriverlo? ");
                if(sovrascrivi){
                    JsonUtil.sovrascriviFileScambio(path);
                }
            }
        } else {
            System.out.println("Errore nell'importazione della configurazione");
        }
    }

    private void importaGerarchia() {
        String strPath = InputDati.leggiStringaNonVuota("Inserisci il percorso del file Gerarchia: ");
        Path path = Path.of(strPath);
        if (strPath.endsWith(".json") && Files.exists(path)){
            if(JsonUtil.checkGerarchiaExists(path)){
                boolean sovrascrivi = InputDati.yesOrNo("Esiste già un file gerarchia con questo nome, vuoi sovrascriverlo? ");
                if(sovrascrivi)
                    JsonUtil.sovrascriviFileGerarchia(path);
            }
            else
                JsonUtil.scriviFileGerarchia(path);
        } else {
            System.out.println("Errore nell'importazione della configurazione");
        }
    }

    /*

    boolean estensioneOk;

        do{
            String nomeFile =InputDati.leggiStringaNonVuota("Inserisci il nome del file da importare:");
            estensioneOk = !nomeFile.endsWith(".json");
            if(estensioneOk){
                System.out.println("Il file deve essere di tipo json");
                return;
            }

            try {
                Files.copy(Path.of(nomeFile), Path.of("files/scambio/"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }while(!estensioneOk);
    private boolean importaGerarchia(file){
        Gerarchia g = JSONUTIL.readGerarchie(file);
        if(g != null){
            g.checkGerarchia();
            return true;
        }
        else{
            System.out.println("\nErrore nell'importazione della gerarchia");
            return false;
        }

    }
     */
}
