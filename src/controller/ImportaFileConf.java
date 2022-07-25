package controller;

import model.user.Utente;
import utility.InputDati;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImportaFileConf implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.importaFile(utente);
        return null;
    }

    private void importaFile(Utente utente) {
        System.out.println("Importa File Configurazione");
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

    }

    /*
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
