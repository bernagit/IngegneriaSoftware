package controller.handlers;

import controller.Handler;
import db.JsonManager;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.user.Session;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaGerarchie implements Handler {
    private JsonManager jsonManager = JsonManager.getInstance();

    @Override
    public Session execute(Session session, View view) {
        this.visualizza(view);
        return session;
    }

    private void visualizza(View view) {
        List<Gerarchia> gerarchiaList = jsonManager.readGerarchie();
        if (gerarchiaList != null && gerarchiaList.size() != 0) {
            ArrayList<String> voci = new ArrayList<>();
            for (Gerarchia gerarchia : gerarchiaList)
                voci.add(gerarchia.getNomeRadice());
            view.createMenu("Gerarchia da visualizzare");
            view.setVociMenu(voci);
            //visualizzazione gerarchia
            Gerarchia ger = gerarchiaList.get(view.scegliVoceMenu());
            view.printGerarchia(ger);
            //visualizzazione sottocategorie della gerarchia selezionata
            boolean end = false;
            ArrayList<Categoria> sottocategorie = ger.getRadice().getFigli();
            ArrayList<Categoria> sottocategorieFoglia = new ArrayList<>();
            for (Categoria cat : sottocategorie) {
                view.printCategoria(cat);
                ArrayList<Categoria> catFoglia = (ArrayList<Categoria>) cat.getCategorieFoglia();
                if (catFoglia != null && catFoglia.size() > 0) {
                    for (Categoria foglia : catFoglia)
                        view.printCategoria(foglia);
                }
            }
        } else
            view.print("\nNessuna gerarchia Inserita");
    }

}
