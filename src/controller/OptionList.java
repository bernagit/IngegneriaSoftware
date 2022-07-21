package controller;
import model.user.Configuratore;
import model.user.Fruitore;

import java.util.ArrayList;
import java.util.List;

public class OptionList {
    final private List<Option> voci = new ArrayList<>();
    public OptionList(){}
    public Option getOption(int n){
        return voci.get(n);
    }
    public ArrayList<String> getConfOptionList(Configuratore conf){
        if(conf != null){
            this.setLoggedOptionConf();
        }
        else{
            this.setOptionConf();
        }
        ArrayList<String> temp = new ArrayList<>();
        for (Option opt: voci){
            temp.add(opt.getLabel());
        }
        return temp;
    }
    public ArrayList<String> getFruitOptionList(Fruitore fruitore) {
        if (fruitore != null)
            this.setLoggedOptionFruit();
        else
            this.setOptionFruit();
        ArrayList<String> temp = new ArrayList<>();
        for (Option opt: voci){
            temp.add(opt.getLabel());
        }
        return temp;
    }
    private void setOptionConf(){
        voci.clear();
        voci.add(new Option("Esci", new Exit()));
        voci.add(new Option("Login", new LoginConf()));
        voci.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
    }
    private void setLoggedOptionConf(){
        voci.clear();
        voci.add(new Option("Esci", new Exit()));
        voci.add(new Option("Logout", new Logout()));
        voci.add(new Option("Visualizza Gerarchie", new VisualizzaGerarchie()));
        voci.add(new Option("Inserisci Gerarchia", new InserisciGerarchia()));
        voci.add(new Option("Aggiungi Scambio", new InserisciScambio()));
        voci.add(new Option("Visualizza Offerte Aperte", new VisualizzaOfferte()));
        voci.add(new Option("Visualizza offerte Chiuse o In Scambio", new VisualizzaOfferteConfig()));
    }
    private void setOptionFruit() {
        voci.clear();
        voci.add(new Option("Esci", new Exit()));
        voci.add(new Option("Login", new LoginFruit()));
    }
    private void setLoggedOptionFruit() {
        voci.clear();
        voci.add(new Option("Esci", new Exit()));
        voci.add(new Option("Logout", new Logout()));
        voci.add(new Option("Visualizza Scambi", new VisualizzaScambi()));
        voci.add(new Option("Pubblica Offerta", new PubblicaOfferta()));
        voci.add(new Option("Visualizza Offerte Aperte", new VisualizzaOfferte()));
        voci.add(new Option("Visualizza o Modifica Offerte Inserite", new VisualizzaOfferteProprietario()));
        voci.add(new Option("Baratta Oggetto", new BarattaOfferta()));
        voci.add(new Option("Accetta Baratto", new AccettaBaratto()));
        voci.add(new Option("Visualizza e/o Accetta Appuntamenti", new ModificaAppuntamento()));
    }
}
