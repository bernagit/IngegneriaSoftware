package controller;

import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Fruitore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PubblicaOffertaTest {
    PubblicaOfferta pubblica = new PubblicaOfferta();
    Offerta offerta;
    InputStream systemIn;

    @Test
    void inserisciOfferta() {
        File file = new File("./src/test/cases/PubblicaArticolo");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        offerta = pubblica.inserisciOfferta(new Fruitore(1000, "test", "test"));

        assertEquals(offerta.getAutore(), "test");
        assertEquals(offerta.getTitolo(), "cronache di narnia");
        assertEquals(offerta.getCategoriaName(), "fumetto");
        assertEquals(offerta.getStatoCorrente(), StatoOfferta.APERTA);
        assertEquals(offerta.getListaCampiCompilati().size(), 3);
        assertEquals(offerta.getListaCampiCompilati().get(2).getContenuto(), "456");
    }
}