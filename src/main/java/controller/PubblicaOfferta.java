package controller;

import model.gerarchia.CampoNativo;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.offerta.Offerta;
import model.offerta.CampoCompilato;
import model.offerta.StatoOfferta;
import model.user.Utente;
import utility.InputDati;
import utility.JsonUtil;
import utility.MyMenu;

import java.util.ArrayList;
import java.util.List;

public class PubblicaOfferta implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.inserisciOfferta(utente);
        return null;
    }

    private void inserisciOfferta(Utente utente) {
        List<Gerarchia> gerarchie = JsonUtil.readGerarchie();
        ArrayList<String> vociGerarchie = new ArrayList<>();
        MyMenu menuGerarchie = new MyMenu("Scelta gerarchia");
        for (Gerarchia gerarchia : gerarchie)
            vociGerarchie.add(gerarchia.getNomeRadice());
        menuGerarchie.setVoci(vociGerarchie);
        //scelta gerarchia
        Gerarchia gerarchiaScelta = gerarchie.get(menuGerarchie.scegli());
        //scelta categoria foglia
        Categoria categoriaFoglia = this.scegliCategoriaFoglia(gerarchiaScelta);
        //inserimento dati offerta
        Offerta offerta = this.inserisciDatiOfferta(categoriaFoglia, utente);
        //set offerta come offerta aperta
        offerta.setStatoCorrente(StatoOfferta.APERTA);

        boolean save = InputDati.yesOrNo("Vuoi salvare l'offerta inserita? ");
        if (save) {
            JsonUtil.writeOfferta(offerta);
            System.out.println("Offerta " + offerta.getTitolo() + " salvato");
        }
    }


    private Categoria scegliCategoriaFoglia(Gerarchia gerarchiaScelta) {
        MyMenu menuCategorie = new MyMenu("Scegli categoria foglia");
        List<Categoria> categorieFoglia = gerarchiaScelta.getRadice().getCategorieFoglia();
        ArrayList<String> voci = new ArrayList<>();
        for (Categoria categoria : categorieFoglia)
            voci.add(categoria.getNome());
        menuCategorie.setVoci(voci);
        return categorieFoglia.get(menuCategorie.scegli());
    }

    private Offerta inserisciDatiOfferta(Categoria categoriaFoglia, Utente utente) {
        String titolo = InputDati.leggiStringaNonVuota("Inserisci titolo offerta: ");
        //creazione articolo
        Offerta offerta = new Offerta(titolo, categoriaFoglia, utente.getUsername());
        //inserimento campi obbligatori
        offerta.addCampiCompilati(this.compilaCampi(categoriaFoglia, true));
        //eventuale inserimento campi non obbligatori
        boolean inserisci = InputDati.yesOrNo("Vuoi inserire eventuali campi non obbligatori? ");
        if (inserisci)
            offerta.addCampiCompilati(this.compilaCampi(categoriaFoglia, false));

        return offerta;
    }

    private List<CampoCompilato> compilaCampi(Categoria categoriaFoglia, boolean obbligatori) {
        List<CampoNativo> campi = null;
        List<CampoCompilato> campiCompilati = new ArrayList<>();
        if (obbligatori)
            campi = categoriaFoglia.getCampiObbligatori();
        else
            campi = categoriaFoglia.getCampiNonObbligatori();

        if (campi != null) {
            System.out.println("Compila i seguenti campi uno per volta:");
            for (CampoNativo campoNativo : campi) {
                String contenuto = InputDati.leggiStringaNonVuota(campoNativo.getNome() + ": ");
                campiCompilati.add(new CampoCompilato(campoNativo, contenuto));
            }
        } else
            System.out.println("Non sono presenti campi da compilare");

        return campiCompilati;
    }
}
