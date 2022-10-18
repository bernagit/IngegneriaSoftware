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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccettaBarattoTest {
    InputStream systemIn;
    Offerta offertaF1;
    Offerta offertaF2;
    PubblicaOfferta pubblica;
    Baratto baratto;
    BarattaOfferta barattaOfferta;
    AccettaBaratto accetta;
    Fruitore fruitore1;
    Fruitore fruitore2;


    @Test
    void accettaBarattoTest(){
        File file = new File("./src/test/cases/AccettaBaratto");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);

        fruitore1 = new Fruitore(1000, "test", "test");
        fruitore2 = new Fruitore(1000, "test1", "test");

        offertaF1 = pubblica.inserisciOfferta(fruitore1);
        offertaF2 = pubblica.inserisciOfferta(fruitore2);

        accetta.accettaBaratto(fruitore1);

        barattaOfferta = new BarattaOfferta();
        baratto = barattaOfferta.barattaOfferta(fruitore1, offertaF1, offertaF2);



        assertEquals(offertaF1.getStatoCorrente(), StatoOfferta.IN_SCAMBIO);
    }

}
