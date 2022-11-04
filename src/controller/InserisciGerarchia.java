package controller;

import model.gerarchia.*;
import model.user.Utente;
import utility.JsonUtil;
import view.InputDati;
import view.MyMenu;
import view.View;

import java.util.ArrayList;

public class InserisciGerarchia implements Handler {
    @Override
    public Utente execute(Utente utente, View view) {
        inserisciGerarchia(view);
        return null;
    }

    private void inserisciGerarchia(View view) {
        String nome = this.inserisciNome();
        String descrizione = InputDati.leggiStringaNonVuota("Inserisci descrizione categoria: ");
        ArrayList<CampoNativo> campi = new ArrayList<>();

        //aggiunta campi obbligatori
        campi.add(new CampoNativo("Stato di conservazione", true));
        campi.add(new CampoNativo("Descrizione libera", false));

        //aggiunta campi da parte dell'utente
        Categoria radice = new Categoria(nome, descrizione, campi, null);
        //aggiunta campi categoria radice
        this.aggiungiCampi(radice, radice);
        //inserimento categorie figlie
        boolean inserimentoMinimo = false;
        do {
            System.out.println("Inserisci almeno 2 sottocategorie:");
            Categoria padre = this.scegliPadre(radice);
            int catInserite = 0;
            boolean inserisciCat = true;
            do {
                this.inserisciFiglio(catInserite+1, padre, radice);
                catInserite++;
                if (catInserite >= 2) {
                    inserisciCat = InputDati.yesOrNo("Vuoi proseguire nell'inserimento di una sottocategoria di "+padre.getNome()+" ?");
                }
            } while (catInserite < 2 || inserisciCat);
            inserimentoMinimo = InputDati.yesOrNo("Vuoi inserire altre sottocategorie?");
        }while (inserimentoMinimo);
        Gerarchia gerarchia = new Gerarchia(radice);
        System.out.println("Vuoi salvare la gerarchia inserita?\n");
        boolean save = InputDati.yesOrNo("");
        if (save) {
            JsonUtil.writeGerarchia(gerarchia);
            System.out.println("Gerarchia salvata");
        }
        else
            System.out.println("Gerarchia non salvata");
    }
    private String inserisciNome() {
        boolean nomeRipetuto = true;
        String nome;
        do{
            nome = InputDati.leggiStringaNonVuota("Inserisci il nome della categoria radice: ");
            if(JsonUtil.checkNomeGerarchiaRipetuto(nome))
                System.out.println("Nome categoria radice già presente, inseriscine un altro");
            else
                nomeRipetuto = false;
        }while(nomeRipetuto);
        return nome;
    }
    private void aggiungiCampi(Categoria radice, Categoria categoria){
        boolean inserisci = false;
        do{
            inserisci = InputDati.yesOrNo("Vuoi inserire un campo? ");
            if (inserisci){
                boolean ripetuto = true;
                String nomeCampo;
                do{
                    nomeCampo = InputDati.leggiStringaNonVuota("Inserisci campo nativo: ");
                    ripetuto = categoria.checkCampoRipetuto(nomeCampo);
                    if (ripetuto)
                        System.out.println("ATTENZIONE: campo già inserito");
                }while (ripetuto);
                boolean obbligatorio = InputDati.yesOrNo("Il campo è obbligatorio? ");
                categoria.addSingoloCampo(new CampoNativo(nomeCampo, obbligatorio));
            }
        }while (inserisci);
    }
    private Categoria scegliPadre(Categoria radice) {
        MyMenu menu = new MyMenu("Scelta categoria padre");
        ArrayList<Categoria> categorie = radice.getStrutturaCompleta();
        ArrayList<String> voci = new ArrayList<>();
        for(Categoria cat: categorie)
            voci.add(cat.getNome());
        menu.setVoci(voci);
        String nomeCatSelezionata = voci.get(menu.scegli());
        //restituisce la categoria scelta nel menu (quella di cui si vuole inserire dei figli (almeno 2))
        for(Categoria cat: categorie)
            if(cat.getNome().equals(nomeCatSelezionata))
                return cat;
        return null;
    }

    private void inserisciFiglio(int num, Categoria padre, Categoria radice) {
        boolean nomeRipetuto = true;
        String nome;
        do {
            nome = InputDati.leggiStringaNonVuota("nome sottocategoria " + num + ": ");
            nomeRipetuto = radice.checkNomeRipetuto(nome);
            if(nomeRipetuto){
                System.out.println("Attenzione: nome categoria già presente all'interno della gerarchia");
            }
            //controllo nome padre
        }while (nomeRipetuto);
        String descrizione = InputDati.leggiStringaNonVuota("descrizione: ");
        //creazione nuovo figlio
        Categoria figlio = new Categoria(nome, descrizione, padre.getCampi(), padre.getNome());
        this.aggiungiCampi(radice, figlio);
        //aggiungo figlio alla categoria padre
        padre.addSingoloFiglio(figlio);
    }
}