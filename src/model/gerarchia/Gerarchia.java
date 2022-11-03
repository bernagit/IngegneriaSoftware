package model.gerarchia;

public class Gerarchia {
    private final Categoria radice;

    public Gerarchia(Categoria radice) {
        this.radice = radice;
    }

    public String getNomeRadice(){
        return this.radice.getNome();
    }

    public Categoria getRadice(){
        return this.radice;
    }
    public String toString() {
        return radice.toString();
    }
}
