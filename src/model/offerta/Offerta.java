package model.offerta;

import model.gerarchia.Categoria;

import java.util.List;

public class Offerta {
    private String titolo;
    private Categoria categoria;
    private List<CampoCompilato> listaCampiCompilati;
    private String autore;
    private List<StatoOfferta> stati;
    private StatoOfferta statoCorrente;

    public Offerta(String titolo, Categoria categoria, String autore) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.autore = autore;
    }

    public void setStati(List<StatoOfferta> stati) {
        this.stati = stati;
    }

    public void setStatoCorrente(StatoOfferta statoCorrente) {
        this.statoCorrente = statoCorrente;
    }

    public void addCampiCompilati(List<CampoCompilato> listaCampiCompilati){
        this.listaCampiCompilati = listaCampiCompilati;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public StatoOfferta getStatoCorrente() {
        return statoCorrente;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nOfferta: ").append(titolo)
                .append("\nCategoria: ").append(categoria.getNome())
                .append("\nAutore pubblicazione: ").append(autore)
                .append("\nDescrizione Oggetto: ");
        for (CampoCompilato campoCompilato: listaCampiCompilati){
            stringBuilder.append("\n\t").append(campoCompilato.getCampo().getNome())
                    .append(": ").append(campoCompilato.getContenuto());
        }
        return stringBuilder.toString();
    }

    public String getCategoriaName() {
        return categoria.getNome();
    }
}
