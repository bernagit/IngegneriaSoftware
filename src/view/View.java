package view;

import model.baratto.Appuntamento;
import model.gerarchia.CampoNativo;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.offerta.Offerta;
import model.scambio.Scambio;

import java.util.ArrayList;

public interface View {
    void print(String str);
    void createMenu(String title);
    void setVociMenu(ArrayList<String> voci);
    void clearVociMenu();
    void addVoceMenu(String voce);
    int scegliVoceMenu();
    void setTitoloMenu(String titolo);
    int getIntPos(String s);
    int getInt(String s);
    int getInt(String s, int i, int i1);
    void printGerarchia(Gerarchia gerarchia);
    void printCategoria(Categoria categoria);
    void printCampoNativo(CampoNativo campo);
    String getString(String s);
    boolean getBoolean(String s);
    void printScambio(Scambio scambio);
    void printOfferta(Offerta offerta);
    void printAppuntamento(Appuntamento appuntamento);
}
