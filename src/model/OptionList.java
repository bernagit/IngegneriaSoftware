package model;
import controller.*;

import java.util.ArrayList;

public class OptionList {
    final private ArrayList<Option> voci = new ArrayList<>();
    String[] arr;
    public OptionList(){}
    public void setOption(){
        voci.clear();
        voci.add(new Option("Esci", new Exit()));
        voci.add(new Option("Login", new Login()));
        voci.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
    }
    public void setLoggedOption(){
        voci.clear();
        voci.add(new Option("Esci", new Exit()));
        voci.add(new Option("Logout", new Logout()));
        voci.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
        voci.add(new Option("Inserisci Gerarchia", new InserisciGerarchia()));
    }

    public ArrayList<String> getOptionList(boolean logged){
        if(logged){
            this.setLoggedOption();
        }
        else{
            this.setOption();
        }
        ArrayList<String> temp = new ArrayList<>();
        for (Option opt: voci){
            temp.add(opt.getLabel());
        }
        return temp;
    }
    public Option getOption(int n){
        return voci.get(n);
    }
}
