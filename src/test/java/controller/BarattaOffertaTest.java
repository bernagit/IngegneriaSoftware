package controller;

import model.baratto.Baratto;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Fruitore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class BarattaOffertaTest {
    PubblicaOfferta pub = new PubblicaOfferta();
    BarattaOfferta barattaOfferta = new BarattaOfferta();
    Baratto baratto;
    InputStream systemIn;
    @Test
    void barattaOfferta() {
        File file = new File("./src/test/cases/BarattaOfferta");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);

        Offerta off1 = pub.inserisciOfferta(new Fruitore(1000, "test", "test"));
        Offerta off2 = pub.inserisciOfferta(new Fruitore(1000, "test1", "1"));

        assertEquals(off2.getCategoria().getNome(), off1.getCategoria().getNome());

        baratto = barattaOfferta.barattaOfferta(new Fruitore(1000, "test", "test"), off1, off2);

        assertEquals(baratto.getOffertaA().getCategoria().getNome(), baratto.getOffertaB().getCategoria().getNome());
        assertEquals(baratto.getOffertaA().getStatoCorrente(), StatoOfferta.ACCOPPIATA);
        assertEquals(baratto.getOffertaB().getStatoCorrente(), StatoOfferta.SELEZIONATA);
        assertEquals(baratto.getDecisore(), "test");
        assertNull(baratto.getAppuntamento());
        assertNotEquals(baratto.getUtenteA(), baratto.getUtenteB());
    }
}