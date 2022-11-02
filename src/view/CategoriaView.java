package view;

import model.gerarchia.CampoNativo;
import model.gerarchia.Categoria;
import java.util.ArrayList;

public class CategoriaView {

    public void printCategoriaDetails(String padre, String nome, String descrizione, ArrayList<CampoNativo> campi){
        System.out.println("\nSottocategoria di " + padre);
        System.out.println("Categoria: " + nome);
        System.out.println("Descrizione: " + descrizione);
        System.out.println("Campi:");
        campi.forEach(System.out::println);
    }

}
