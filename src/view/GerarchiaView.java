package view;

import model.gerarchia.CampoNativo;
import model.gerarchia.Gerarchia;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GerarchiaView {

    public void printGerarchiaDetails(String nomeRadice, String radice, ArrayList<CampoNativo> campi){
        System.out.println("\nGerarchia: " + nomeRadice);
        System.out.println("Descrizione: " + radice);
        campi.forEach(System.out::println);
    }

}
