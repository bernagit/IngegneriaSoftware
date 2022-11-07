package controller.handlers;

import controller.Handler;
import model.gerarchia.Categoria;
import model.user.Utente;
import db.JsonUtil;
import model.gerarchia.Gerarchia;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaGerarchie implements Handler {
    @Override
    public Utente execute(Utente utente, View view) {
        this.visualizza(view);
        return null;
    }
    private void visualizza(View view) {
        List<Gerarchia> gerarchiaList = JsonUtil.readGerarchie();
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
            do {
                for (Categoria cat : sottocategorie) {
                    view.printCategoria(cat);
                    if (cat.getFigli() != null) sottocategorieFoglia.addAll(cat.getFigli());
                    sottocategorie = sottocategorieFoglia;
                    sottocategorieFoglia = null;
                }
                if (sottocategorie == null) end = true;
            } while (!end);
        } else
            view.print("\nNessuna gerarchia Inserita");
    }

}
