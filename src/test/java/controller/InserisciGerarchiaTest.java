package controller;

import model.gerarchia.Gerarchia;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InserisciGerarchiaTest {
    InserisciGerarchia inserisci = new InserisciGerarchia();
    Gerarchia gerarchia;
    InputStream systemIn;

    @Test
    void inserisciGerarchia() {

        File file = new File("./src/test/cases/InserisciGerarchia");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non torovato");
        }
        System.setIn(systemIn);
        gerarchia = inserisci.inserisciGerarchia();
        assertEquals(gerarchia.getNomeRadice(), "libri");
        assertEquals(gerarchia.getRadice().getPadre(), null);
    }
}