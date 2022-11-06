package view;

import model.gerarchia.CampoNativo;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.scambio.IntervalloOrario;
import model.scambio.Scambio;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class View {
    MyMenu menu;
    public void print(String str){
        System.out.println(str);
    }
    public void createMenu(String title){
        menu = new MyMenu(title);
    }
    public void setVociMenu(ArrayList<String> voci) {
        menu.clearVoci();
        menu.setVoci(voci);
    }
    public int scegliVoceMenu() {
        return menu.scegli();
    }
    public void setTitoloMenu(String titolo) {
        menu.setTitolo(titolo);
    }


    public void printGerarchia(Gerarchia gerarchia){
        System.out.println("\nGerarchia: " + gerarchia.getNomeRadice());
        System.out.println("Descrizione: " + gerarchia.getRadice().getDescrizione());
        gerarchia.getRadice().getCampi().forEach(this::printCampoNativo);
    }

    public void printCategoria(Categoria categoria){
        System.out.println("\nSottocategoria di " + categoria.getPadre());
        System.out.println("Categoria: " + categoria.getNome());
        System.out.println("Descrizione: " + categoria.getDescrizione());
        System.out.println("Campi:");
        categoria.getCampi().forEach(this::printCampoNativo);
    }

    public void printCampoNativo(CampoNativo campo){
        StringBuilder str = new StringBuilder();
        str.append("\t- ").append(campo.getNome());
        if (!campo.isRequired())
            str.append(" (opzionale)");
        System.out.println(str);
    }

    public String getString(String s) {
        return InputDati.leggiStringaNonVuota(s);
    }

    public boolean getBoolean(String s) {
        return InputDati.yesOrNo(s);
    }

    public void printScambio(Scambio scambio) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nPiazza di scambio: ")
                    .append(scambio.getPiazza());

            stringBuilder.append("\nLuoghi:");
            for (String luogo: scambio.getLuoghi())
                stringBuilder.append("\n\t")
                        .append(luogo);

            stringBuilder.append("\nGiorni dello scambio:");
            for (DayOfWeek giorno: scambio.getGiorni())
                stringBuilder.append("\n\t")
                        .append(giorno.getDisplayName(TextStyle.FULL, Locale.getDefault()));

            stringBuilder.append("\nIntervalli orari:");
            for (IntervalloOrario interval: scambio.getIntervalliOrari())
                stringBuilder.append("\n\tOra inizio: ")
                        .append(interval.getOraInizio())
                            .append("\tOra fine: ")
                                .append(interval.getOraFine());

            System.out.println(stringBuilder);
    }

    public int getIntPos(String s) {
        return InputDati.leggiInteroPositivo(s);
    }

    public int getInt(String s){
        return InputDati.leggiIntero(s);
    }

    public int getInt(String s, int i, int i1) {
        return InputDati.leggiIntero(s, i, i1);
    }
}
