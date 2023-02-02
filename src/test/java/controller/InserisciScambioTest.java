package controller;

import model.scambio.IntervalloOrario;
import model.scambio.Scambio;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.DayOfWeek;
import java.util.List;

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

    @Test
    void testInserisciScambioSetGiorniInesistenti(){
        File file = new File("./src/test/black-box/giorniInesistenti");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        System.setOut(ps);
        List<DayOfWeek> giorni = inserisci.inserisciGiorni();

        assertEquals(ps.toString(), "");
    }
    @Test
    void testInserisciScambioSetGiorniEsistenti() {
        File file = new File("./src/test/black-box/giorniEsistenti");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);

        List<DayOfWeek> giorni = inserisci.inserisciGiorni();

        assertNotNull(giorni);
    }

    @Test
    void testInserisciScambioSetOrariErrati() {
        File file = new File("./src/test/black-box/orariInesistenti");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        System.setOut(ps);
        List<IntervalloOrario> orari = inserisci.inserisciIntervalli();

        assertEquals(ps.toString(), "");
    }

    @Test
    void testInserisciScambioSetOrariEsatti() {
        File file = new File("./src/test/black-box/orariEsistenti");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);

        List<IntervalloOrario> orari = inserisci.inserisciIntervalli();

        assertNotNull(orari);
    }
}