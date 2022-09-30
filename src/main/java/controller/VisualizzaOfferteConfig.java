package controller;

import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.user.Utente;
import utility.JsonUtil;
import utility.MyMenu;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaOfferteConfig implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.sceltaGerarchia();
        return null;
    }

    private void sceltaGerarchia() {
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
        //presento a video tutte le offerte aperte
        this.visualizzaOfferteChSc(categoriaFoglia);
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


    private void visualizzaOfferteChSc(Categoria categoria) {
        //cerco offerte IN_SCAMBIO e CHIUSE
        List<Offerta> offerte = JsonUtil.readOfferteByCategoriaAndState(categoria.getNome(), StatoOfferta.CHIUSA);
        offerte.addAll(JsonUtil.readOfferteByCategoriaAndState(categoria.getNome(), StatoOfferta.IN_SCAMBIO));

        if (offerte != null && offerte.size() >= 1) {
            for (Offerta offerta : offerte) {
                System.out.println(offerta);
            }
        } else
            System.out.println("\nNon sono presenti offerte In Scambio o Chiuse per questa categoria");
    }
}
