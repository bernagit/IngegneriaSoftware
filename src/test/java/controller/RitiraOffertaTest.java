package controller;

import model.gerarchia.Categoria;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RitiraOffertaTest {
    VisualizzaOfferteProprietario visualizza;
    Offerta offerta;
    InputStream systemIn;
    @Test
    void ritiraOfferta() {
        offerta = new Offerta("Offerta aperta test", new Categoria("Prova","Test",new ArrayList<>(),""), "Michele");
        offerta.setStatoCorrente(StatoOfferta.APERTA); //di default alla creazione di un'offerta da parte del fruitore

        visualizza = new VisualizzaOfferteProprietario();

        File file = new File("./src/test/cases/RitiraOfferta");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }

        System.setIn(systemIn);
        visualizza.modificaOfferta(offerta);

        assertEquals(offerta.getStatoCorrente(), StatoOfferta.RITIRATA);
    }
}
