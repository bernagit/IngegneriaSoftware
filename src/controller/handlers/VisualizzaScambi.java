package controller.handlers;

import controller.Handler;
import db.JsonManager;
import model.gerarchia.Gerarchia;
import model.scambio.Scambio;
import model.user.Utente;
import view.View;

import java.util.List;

public class VisualizzaScambi implements Handler {
    @Override
    public Utente execute(Utente utente, View view) {
        this.visualizza(view);
        return null;
    }
    private void visualizza(View view) {
        //metodo per visualizzare gli scambi
        List<Gerarchia> gerarchie = JsonManager.readGerarchie();
        Scambio scambio = JsonManager.readScambio();
        if (gerarchie != null) {
            view.print("Gerarchie:");
            for (Gerarchia gerarchia : gerarchie)
                view.printGerarchia(gerarchia);
        }
        else
           view.print("Impossibile visualizzare luoghi di scambio: gerarchie nulle!");

        if(scambio != null)
            view.printScambio(scambio);
        else
            view.print("Impossibile visualizzare scambi: non esistono luoghi di scambio");
    }
}
