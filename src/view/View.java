package view;

import model.gerarchia.CampoNativo;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;

public class View {

    MyMenu menu;
    public void print(String str){
        System.out.println(str);
    }

    public void createMenu(String title){
        menu = new MyMenu(title);
    }
    public void printGerarchia(Gerarchia gerarchia){
        System.out.println("\nGerarchia: " + gerarchia.getNomeRadice());
        System.out.println("Descrizione: " + gerarchia.getRadice().getDescrizione());
        gerarchia.getRadice().getCampi().forEach(this::printCampoNativo);
    }

    public void printCategoria(Categoria categoria){
        System.out.println("\nSottocategoria di " + categoria.getPadre());
        System.out.println("Categoria: " + categoria.getNome());
        System.out.println("Descrizione: " + categoria.getDescrizione());
        System.out.println("Campi:");
        categoria.getCampi().forEach(this::printCampoNativo);
    }

    public void printCampoNativo(CampoNativo campo){
        StringBuilder str = new StringBuilder();
        str.append("\t- ").append(campo.getNome());
        if (!campo.isRequired())
            str.append(" (opzionale)");
        System.out.println(str);
    }

}
