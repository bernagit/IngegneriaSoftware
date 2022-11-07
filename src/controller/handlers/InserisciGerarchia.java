package controller.handlers;

import controller.Handler;
import model.gerarchia.*;
import model.user.Utente;
import utility.JsonUtil;
import view.View;

import java.util.ArrayList;

public class InserisciGerarchia implements Handler {
    @Override
    public Utente execute(Utente utente, View view) {
        inserisciGerarchia(view);
        return null;
    }

    private void inserisciGerarchia(View view) {
        String nome = this.inserisciNome(view);
        String descrizione = view.getString("Inserisci descrizione categoria: ");
        ArrayList<CampoNativo> campi = new ArrayList<>();

        //aggiunta campi obbligatori
        campi.add(new CampoNativo("Stato di conservazione", true));
        campi.add(new CampoNativo("Descrizione libera", false));

        //aggiunta campi da parte dell'utente
        Categoria radice = new Categoria(nome, descrizione, campi, null);
        //aggiunta campi categoria radice
        this.aggiungiCampi(radice, radice, view);
        //inserimento categorie figlie
        boolean inserimentoMinimo = false;
        do {
            view.print("Inserisci almeno 2 sottocategorie:");
            Categoria padre = this.scegliPadre(radice, view);
            int catInserite = 0;
            boolean inserisciCat = true;
            do {
                this.inserisciFiglio(catInserite+1, padre, radice, view);
                catInserite++;
                if (catInserite >= 2) {
                    inserisciCat = view.getBoolean("Vuoi proseguire nell'inserimento di una sottocategoria di "+padre.getNome()+" ?");
                }
            } while (catInserite < 2 || inserisciCat);
            inserimentoMinimo = view.getBoolean("Vuoi inserire altre sottocategorie?");
        }while (inserimentoMinimo);
        Gerarchia gerarchia = new Gerarchia(radice);
        view.print("Vuoi salvare la gerarchia inserita?\n");
        boolean save = view.getBoolean("");
        if (save) {
            JsonUtil.writeGerarchia(gerarchia);
            view.print("Gerarchia salvata");
        }
        else
            view.print("Gerarchia non salvata");
    }
    private String inserisciNome(View view) {
        boolean nomeRipetuto = true;
        String nome;
        do{
            nome = view.getString("Inserisci il nome della categoria radice: ");
            if(JsonUtil.checkNomeGerarchiaRipetuto(nome))
                view.print("Nome categoria radice già presente, inseriscine un altro");
            else
                nomeRipetuto = false;
        }while(nomeRipetuto);
        return nome;
    }
    private void aggiungiCampi(Categoria radice, Categoria categoria, View view){
        boolean inserisci = false;
        do{
            inserisci = view.getBoolean("Vuoi inserire un campo? ");
            if (inserisci){
                boolean ripetuto = true;
                String nomeCampo;
                do{
                    nomeCampo = view.getString("Inserisci campo nativo: ");
                    ripetuto = categoria.checkCampoRipetuto(nomeCampo);
                    if (ripetuto)
                        view.print("ATTENZIONE: campo già inserito");
                }while (ripetuto);
                boolean obbligatorio = view.getBoolean("Il campo è obbligatorio? ");
                categoria.addSingoloCampo(new CampoNativo(nomeCampo, obbligatorio));
            }
        }while (inserisci);
    }
    private Categoria scegliPadre(Categoria radice, View view) {
        view.createMenu("Scelta categoria padre");
        ArrayList<Categoria> categorie = radice.getStrutturaCompleta();
        ArrayList<String> voci = new ArrayList<>();
        for(Categoria cat: categorie)
            voci.add(cat.getNome());
        view.setVociMenu(voci);
        String nomeCatSelezionata = voci.get(view.scegliVoceMenu());
        //restituisce la categoria scelta nel menu (quella di cui si vuole inserire dei figli (almeno 2))
        for(Categoria cat: categorie)
            if(cat.getNome().equals(nomeCatSelezionata))
                return cat;
        return null;
    }

    private void inserisciFiglio(int num, Categoria padre, Categoria radice, View view) {
        boolean nomeRipetuto = true;
        String nome;
        do {
            nome = view.getString("nome sottocategoria " + num + ": ");
            nomeRipetuto = radice.checkNomeRipetuto(nome);
            if(nomeRipetuto){
                view.print("Attenzione: nome categoria già presente all'interno della gerarchia");
            }
            //controllo nome padre
        }while (nomeRipetuto);
        String descrizione = view.getString("descrizione: ");
        //creazione nuovo figlio
        Categoria figlio = new Categoria(nome, descrizione, padre.getCampi(), padre.getNome());
        this.aggiungiCampi(radice, figlio, view);
        //aggiungo figlio alla categoria padre
        padre.addSingoloFiglio(figlio);
    }
}