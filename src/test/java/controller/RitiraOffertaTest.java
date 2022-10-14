package controller;

import model.gerarchia.Categoria;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RitiraOffertaTest {
    Offerta offerta;

    @Test
    void ritiraOfferta() {
        offerta = new Offerta("Offerta aperta test", new Categoria("Prova","Test",new ArrayList<>(),""), "Michele");
        offerta.setStatoCorrente(StatoOfferta.APERTA);

        offerta.setStatoCorrente(StatoOfferta.RITIRATA);
        assertEquals(offerta.getStatoCorrente(), StatoOfferta.RITIRATA);
    }
}
