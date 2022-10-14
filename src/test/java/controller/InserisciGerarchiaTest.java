package controller;

import model.gerarchia.Gerarchia;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

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
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        gerarchia = inserisci.inserisciGerarchia();
        //test
        assertEquals(gerarchia.getNomeRadice(), "scarpe");
        assertEquals(gerarchia.getRadice().getFigli().size(), 3);
        assertEquals(gerarchia.getRadice().getFigli().get(2).getNome(), "eleganti");
        assertEquals(gerarchia.getRadice().getFigli().get(2).getFigli().size(), 2);
        assertEquals(gerarchia.getRadice().getFigli().get(2).getFigli().get(0).getNome(), "mocassini");
        assertTrue(gerarchia.getRadice().getFigli().get(0).isFoglia());
        assertTrue(gerarchia.getRadice().getFigli().get(2).getFigli().get(0).isFoglia());
        assertEquals(gerarchia.getRadice().getFigli().get(2).getFigli().get(0).getPadre(), "eleganti");
        assertEquals(gerarchia.getRadice().getFigli().get(2).getFigli().get(0).getCampi().get(0).getNome(), "Stato di conservazione");
        assertEquals(gerarchia.getRadice().getFigli().get(2).getFigli().get(1).getCampi().get(4).getNome(), "altezza tacco");
        assertTrue(gerarchia.getRadice().getFigli().get(2).getFigli().get(1).getCampi().get(4).isRequired());
    }
}