package controller;

import model.gerarchia.Categoria;
import model.user.Utente;
import utility.JsonUtil;
import model.gerarchia.Gerarchia;
import utility.MyMenu;
import view.CategoriaView;
import view.GerarchiaView;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaGerarchie implements Handler {
    CategoriaView catView;
    GerarchiaView gerView;
    @Override
    public Utente execute(Utente utente, View view) {
        catView = (CategoriaView) view;
        gerView = (GerarchiaView) view;
        this.visualizza();
        return null;
    }
    private void visualizza() {
        List<Gerarchia> gerarchiaList = JsonUtil.readGerarchie();
        if (gerarchiaList != null && gerarchiaList.size() != 0) {
            ArrayList<String> voci = new ArrayList<>();
            for (Gerarchia gerarchia : gerarchiaList)
                voci.add(gerarchia.getNomeRadice());
            MyMenu menu = new MyMenu("Gerarchia da visualizzare");
            menu.setVoci(voci);
            //visualizzazione gerarchia
            Gerarchia ger = gerarchiaList.get(menu.scegli());
            gerView.print(ger);
            //visualizzazione sottocategorie della gerarchia selezionata
            boolean end = false;
            ArrayList<Categoria> sottocategorie = ger.getRadice().getFigli();
            ArrayList<Categoria> sottocategorieFoglia = new ArrayList<>();
            do {
                for (Categoria cat : sottocategorie) {
                    catView.print(cat);
                    if (cat.getFigli() != null) sottocategorieFoglia.addAll(cat.getFigli());
                    sottocategorie = sottocategorieFoglia;
                    sottocategorieFoglia = null;
                }
                if (sottocategorie == null) end = true;
            } while (!end);
        } else System.out.println("\nNessuna gerarchia Inserita");
    }

}
