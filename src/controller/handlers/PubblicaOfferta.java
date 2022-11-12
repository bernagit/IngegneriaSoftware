package controller.handlers;

import controller.Handler;
import db.JsonManager;
import model.gerarchia.*;
import model.offerta.*;
import model.user.Utente;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class PubblicaOfferta implements Handler {
    @Override
    public Utente execute(Utente utente, View view) {
        this.inserisciOfferta(utente, view);
        return null;
    }

    private void inserisciOfferta(Utente utente, View view) {
        List<Gerarchia> gerarchie = JsonManager.readGerarchie();
        ArrayList<String> vociGerarchie = new ArrayList<>();
        view.createMenu("Scelta gerarchia");
        for (Gerarchia gerarchia : gerarchie)
            vociGerarchie.add(gerarchia.getNomeRadice());
        view.setVociMenu(vociGerarchie);
        //scelta gerarchia
        Gerarchia gerarchiaScelta = gerarchie.get(view.scegliVoceMenu());
        //scelta categoria foglia
        Categoria categoriaFoglia = this.scegliCategoriaFoglia(gerarchiaScelta, view);
        //inserimento dati offerta
        Offerta offerta = this.inserisciDatiOfferta(categoriaFoglia, utente, view);
        //set offerta come offerta aperta
        offerta.setStatoCorrente(StatoOfferta.APERTA);

        boolean save = view.getBoolean("Vuoi salvare l'offerta inserita? ");
        if (save) {
            JsonManager.writeOfferta(offerta);
            view.print("Offerta " + offerta.getTitolo() + " salvato");
        }
    }


    private Categoria scegliCategoriaFoglia(Gerarchia gerarchiaScelta, View view) {
        view.createMenu("Scegli categoria foglia");
        List<Categoria> categorieFoglia = gerarchiaScelta.getRadice().getCategorieFoglia();
        ArrayList<String> voci = new ArrayList<>();
        for (Categoria categoria : categorieFoglia)
            voci.add(categoria.getNome());
        view.setVociMenu(voci);
        return categorieFoglia.get(view.scegliVoceMenu());
    }

    private Offerta inserisciDatiOfferta(Categoria categoriaFoglia, Utente utente, View view) {
        String titolo = view.getString("Inserisci titolo offerta: ");
        //creazione articolo
        Offerta offerta = new Offerta(titolo, categoriaFoglia, utente.getUsername());
        //inserimento campi obbligatori
        offerta.addCampiCompilati(this.compilaCampi(categoriaFoglia, true, view));
        //eventuale inserimento campi non obbligatori
        boolean inserisci = view.getBoolean("Vuoi inserire eventuali campi non obbligatori? ");
        if (inserisci)
            offerta.addCampiCompilati(this.compilaCampi(categoriaFoglia, false, view));

        return offerta;
    }

    private List<CampoCompilato> compilaCampi(Categoria categoriaFoglia, boolean obbligatori, View view) {
        List<CampoNativo> campi = null;
        List<CampoCompilato> campiCompilati = new ArrayList<>();
        if (obbligatori)
            campi = categoriaFoglia.getCampiObbligatori();
        else
            campi = categoriaFoglia.getCampiNonObbligatori();
        if (campi != null) {
            view.print("Compila i seguenti campi uno per volta:");
            for (CampoNativo campoNativo : campi) {
                String contenuto = view.getString(campoNativo.getNome() + ": ");
                campiCompilati.add(new CampoCompilato(campoNativo, contenuto));
            }
        } else
            view.print("Non sono presenti campi da compilare");
        return campiCompilati;
    }
}
