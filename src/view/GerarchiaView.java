package view;

import model.gerarchia.CampoNativo;
import model.gerarchia.Gerarchia;

import java.util.ArrayList;

public class GerarchiaView implements View{
    Gerarchia gerarchia;
    public void print(Object obj){
        gerarchia = (Gerarchia) obj;
        System.out.println("\nGerarchia: " + gerarchia.getNomeRadice());
        System.out.println("Descrizione: " + gerarchia.getRadice().getDescrizione());
        gerarchia.getRadice().getCampi().forEach(e -> print(e));
    }
}
