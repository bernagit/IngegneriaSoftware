package model.gerarchia;

public class CampoNativo {
    private final String nomeCampo;
    private final boolean required;
    public CampoNativo(String nomeCampo, boolean required) {
        this.nomeCampo = nomeCampo;
        this.required = required;
    }
    public String getNome() {
        return nomeCampo;
    }
    public boolean isRequired() {
        return required;
    }

}