package controller;

import model.scambio.Scambio;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class InserisciScambioTest {
    InserisciScambio inserisci = new InserisciScambio();

    Scambio scambio;

    InputStream systemIn;
    @Test
    void inserisciScambio() {
        File file = new File("./src/test/cases/InserisciScambio");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        scambio = inserisci.inserisciScambio();

        assertEquals(scambio.getPiazza(), "cremona");
        assertEquals(scambio.getLuoghi().size(), 2);
        assertEquals(scambio.getLuoghi().get(1), "porta venezia");
        assertEquals(scambio.getGiorni().size(), 3);
        assertEquals(scambio.getGiorni().get(2), DayOfWeek.SUNDAY);
        assertEquals(scambio.getIntervalliOrari().size(), 2);
        assertEquals(scambio.getIntervalliOrari().get(1).getOraInizio().toString(), "20:00");
        assertEquals(scambio.getScadenzaProposta(), 10);

    }
}