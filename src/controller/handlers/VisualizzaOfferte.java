package controller.handlers;

import controller.Handler;
import model.user.Session;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import db.JsonManager;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaOfferte implements Handler {
    private JsonManager jsonManager = JsonManager.getInstance();
    @Override
    public Session execute(Session session, View view) {
        this.sceltaGerarchia(view);
        return session;
    }

    private void sceltaGerarchia(View view) {
        List<Gerarchia> gerarchie = jsonManager.readGerarchie();
        ArrayList<String> vociGerarchie = new ArrayList<>();
        view.createMenu("Scelta gerarchia");
        for (Gerarchia gerarchia : gerarchie)
            vociGerarchie.add(gerarchia.getNomeRadice());
        view.setVociMenu(vociGerarchie);
        //scelta gerarchia
        Gerarchia gerarchiaScelta = gerarchie.get(view.scegliVoceMenu());
        //scelta categoria foglia
        Categoria categoriaFoglia = this.scegliCategoriaFoglia(gerarchiaScelta, view);
        //presento a video tutte le offerte aperte
        this.visualizzaOfferteAperte(categoriaFoglia, view);
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

    private void visualizzaOfferteAperte(Categoria categoria, View view) {
        List<Offerta> offerte = jsonManager.readOfferteByCategoriaAndState(categoria.getNome(), StatoOfferta.APERTA);
        if (offerte != null && offerte.size() >= 1) {
            for (Offerta offerta : offerte) {
                view.printOfferta(offerta);
            }
        } else
            view.print("\nNon sono presenti offerte Aperte per questa categoria");
    }
}
