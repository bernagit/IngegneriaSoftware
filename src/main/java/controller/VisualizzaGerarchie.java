package controller;

import model.gerarchia.Categoria;
import model.user.Utente;
import utility.JsonUtil;
import model.gerarchia.Gerarchia;
import utility.MyMenu;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaGerarchie implements Action {
    @Override
    public Utente execute(Utente utente) {
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
            Gerarchia gerarchia = gerarchiaList.get(menu.scegli());
            System.out.println("\nGerarchia: " + gerarchia.getNomeRadice());
            Categoria radice = gerarchia.getRadice();
            System.out.println("Descrizione: " + radice.getDescrizione());
            System.out.println("Campi:");
            radice.getCampi().forEach(System.out::println);
            //visualizzazione sottocategorie della gerarchia selezionata
            boolean end = false;
            ArrayList<Categoria> sottocategorie = radice.getFigli();
            ArrayList<Categoria> sottocategorieFoglia = new ArrayList<>();
            do {
                for (Categoria categoria : sottocategorie) {
                    System.out.println("\nSottocategoria di " + categoria.getPadre());
                    System.out.println("Categoria: " + categoria.getNome());
                    System.out.println("Descrizione: " + categoria.getDescrizione());
                    System.out.println("Campi:");
                    categoria.getCampi().forEach(System.out::println);
                    if (categoria.getFigli() != null) sottocategorieFoglia.addAll(categoria.getFigli());
                    sottocategorie = sottocategorieFoglia;
                    sottocategorieFoglia = null;
                }
                if (sottocategorie == null) end = true;
            } while (!end);
        } else System.out.println("\nNessuna gerarchia Inserita");
    }

}
