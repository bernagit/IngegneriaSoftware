package controller.handlers;

import controller.Handler;
import model.user.Session;
import db.JsonManager;
import model.gerarchia.Gerarchia;
import model.scambio.Scambio;
import view.View;

import java.util.List;

public class VisualizzaScambi implements Handler {
    private JsonManager jsonManager = JsonManager.getInstance();
    @Override
    public Session execute(Session session, View view) {
        this.visualizza(view);
        return session;
    }
    private void visualizza(View view) {
        //metodo per visualizzare gli scambi
        List<Gerarchia> gerarchie = jsonManager.readGerarchie();
        Scambio scambio = jsonManager.readScambio();
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
