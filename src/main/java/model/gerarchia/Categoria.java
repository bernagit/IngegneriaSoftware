package model.gerarchia;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private final String nome;
    private final String descrizione;
    private ArrayList<CampoNativo> campi;
    private ArrayList<Categoria> figli;
    private final String padre;

    public Categoria(String nome, String descrizione, ArrayList<CampoNativo> campi, String padre) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.campi = new ArrayList<>();
        if (campi != null)
            this.campi.addAll(campi);
        else
            this.campi = campi;
        this.padre = padre;
    }

    public void addSingoloCampo(CampoNativo campo) {
        if (this.campi == null)
            this.campi = new ArrayList<>();
        this.campi.add(campo);
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getPadre() {
        return padre;
    }

    public boolean isRadice() {
        return padre == null;
    }

    public boolean isFoglia() {
        return figli == null;
    }

    public void addSingoloFiglio(Categoria figlia) {
        if (this.figli == null)
            this.figli = new ArrayList<>();
        this.figli.add(figlia);
    }

    public ArrayList<CampoNativo> getCampi() {
        return this.campi;
    }

    public ArrayList<Categoria> getStrutturaCompleta() {
        ArrayList<Categoria> strutturaCompleta = new ArrayList<>();
        if (this.isFoglia()) {
            strutturaCompleta.add(this);
            return strutturaCompleta;
        }
        for (Categoria c : this.figli) {
            if (!isFoglia())
                strutturaCompleta.addAll(c.getStrutturaCompleta());
            else
                strutturaCompleta.add(c);
        }
        return strutturaCompleta;
    }

    public ArrayList<Categoria> getFigli() {
        return figli;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\nNome: ").append(nome)
                .append("\nDescrizione: ").append(descrizione)
                .append("\nCampi:");
        for (CampoNativo cmp : this.campi) {
            str.append(cmp.toString());
        }
        if (!isFoglia()) {
            str.append("\nFigli:");
            for (Categoria cat : this.figli)
                str.append("\t").append(cat.toString());
        }
        return str.toString();
    }

    public boolean isCampoPresente(String nomeCampo) {
        if (this.campi != null)
            for (CampoNativo campo : this.campi)
                if (campo.getNome().equalsIgnoreCase(nomeCampo))
                    return true;
        return false;
    }

    public boolean checkCampoRipetuto(String nomeCampo) {
        return this.isCampoPresente(nomeCampo);
    }

    public boolean checkNomeRipetuto(String nome) {
        for (Categoria cat : this.getStrutturaCompleta())
            if (cat.getNome().equalsIgnoreCase(nome))
                return true;
        return false;
    }

    public List<Categoria> getCategorieFoglia() {
        ArrayList<Categoria> categorieFoglia = new ArrayList<>();
        if (this.figli != null) {
            for (Categoria c : this.figli) {
                if (c.isFoglia())
                    categorieFoglia.add(c);
                else
                    categorieFoglia.addAll(c.getCategorieFoglia());
            }
        }
        return categorieFoglia;
    }

    public List<CampoNativo> getCampiObbligatori() {
        List<CampoNativo> campi = new ArrayList<>();
        for (CampoNativo campo : this.campi) {
            if (campo.isRequired())
                campi.add(campo);
        }
        return campi;
    }

    public List<CampoNativo> getCampiNonObbligatori() {
        List<CampoNativo> campi = new ArrayList<>();
        for (CampoNativo campo : this.campi) {
            if (!campo.isRequired())
                campi.add(campo);
        }
        return campi;
    }
}

