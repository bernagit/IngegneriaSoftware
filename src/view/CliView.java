package view;

import model.baratto.Appuntamento;
import model.gerarchia.CampoNativo;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.offerta.CampoCompilato;
import model.offerta.Offerta;
import model.scambio.IntervalloOrario;
import model.scambio.Scambio;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class CliView implements View{
    Menu menu;
    public void print(String str){
        System.out.println(str);
    }
    public void createMenu(String title){
        menu = new Menu(title);
    }
    public void setVociMenu(ArrayList<String> voci) {
        menu.clearVoci();
        menu.setVoci(voci);
    }
    public void clearVociMenu(){
        menu.clearVoci();
    }
    public void addVoceMenu(String voce){
        menu.addVoce(voce);
    }
    public int scegliVoceMenu() {
        return menu.scegli();
    }
    public void setTitoloMenu(String titolo) {
        menu.setTitolo(titolo);
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
    public void printOfferta(Offerta offerta) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nOfferta: ").append(offerta.getTitolo())
                .append("\nCategoria: ").append(offerta.getCategoria().getNome())
                //().append("\nAutore pubblicazione: ").append(autore)
                .append("\nDescrizione Oggetto: ");
        for (CampoCompilato campoCompilato: offerta.getListaCampiCompilati()){
            stringBuilder.append("\n\t").append(campoCompilato.getCampo().getNome())
                    .append(": ").append(campoCompilato.getContenuto());
        }
        stringBuilder.append("\nStatoOfferta: ").append(offerta.getStatoCorrente().toString());

        System.out.println(stringBuilder);
    }
    public void printAppuntamento(Appuntamento appuntamento){
        String str = "\nLuogo: " + appuntamento.getLuogo() +
                "\nOra: " + appuntamento.getDataOra() +
                "\nGiorno: " + appuntamento.getGiorno().name();
        System.out.println(str);
    }
}
