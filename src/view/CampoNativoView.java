package view;

import model.gerarchia.CampoNativo;

public class CampoNativoView implements View {
    CampoNativo campo;
    @Override
    public void print(Object obj) {
        campo = (CampoNativo) obj;
        StringBuilder str = new StringBuilder();
        str.append("\t- ").append(campo.getNome());
        if (!campo.isRequired())
            str.append(" (opzionale)");
        System.out.println(str);
    }
}
