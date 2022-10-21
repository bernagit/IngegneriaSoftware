package controller;

import model.scambio.Scambio;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ImportaFileConfTest {
    ImportaFileConf importaFileConf = new ImportaFileConf();
    InputStream systemIn;
    ByteArrayOutputStream systemOut = new ByteArrayOutputStream();

    @Test
    void importaConfigurazione() {
        File file = new File("./src/test/cases/ImportaFileConf");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        System.setOut(new PrintStream(systemOut));
        importaFileConf.importaConfigurazione();
        assertEquals(systemOut.toString(), "Inserisci il percorso del file di configurazione: Esiste già un file di configurazione, vuoi sovrascriverlo? (S/N): File di configurazione importato con successo\n");
    }

    @Test
    void importaGerarchia(){
        File file = new File("./src/test/cases/ImportaFileGer");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        System.setOut(new PrintStream(systemOut));
        importaFileConf.importaGerarchia();
        assertEquals(systemOut.toString(), "Inserisci il percorso del file Gerarchia: Esiste già un file gerarchia con questo nome, vuoi sovrascriverlo? (S/N): File Gerarchia non importato\n");
    }
}