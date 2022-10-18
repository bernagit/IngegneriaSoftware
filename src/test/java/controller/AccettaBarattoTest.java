package controller;

import model.gerarchia.Categoria;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Fruitore;
import org.junit.jupiter.api.Test;
import utility.JsonUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccettaBarattoTest {
    InputStream systemIn;
    Offerta offertaF1;
    Offerta offertaF2;
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

        fruitore1 = new Fruitore(1000, "test1", "test");
        fruitore2 = new Fruitore(1000, "test2", "test");

        offertaF1 = new Offerta("Offerta aperta test1", new Categoria("Prova","Test",new ArrayList<>(),""), "test1");
        offertaF1.setStatoCorrente(StatoOfferta.APERTA);
        JsonUtil.writeOfferta(offertaF1);

        offertaF2 = new Offerta("Offerta aperta test2", new Categoria("Prova","Test",new ArrayList<>(),""), "test2");
        offertaF2.setStatoCorrente(StatoOfferta.APERTA);
        JsonUtil.writeOfferta(offertaF2);

        barattaOfferta = new BarattaOfferta();
        barattaOfferta.inserisciBaratto(offertaF2,offertaF1);
        assertEquals(offertaF2.getStatoCorrente(), StatoOfferta.ACCOPPIATA);

        accetta = new AccettaBaratto();
        accetta.accettaBaratto(fruitore1);

        //assertEquals(offertaF1.getStatoCorrente(), StatoOfferta.IN_SCAMBIO);

    }

}
