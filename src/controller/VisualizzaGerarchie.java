package controller;

import data.JsonUtil;
import model.Action;
import structure.Gerarchia;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaGerarchie implements Action {
    private void visualizza() {
        List<Gerarchia> gerarchiaList = JsonUtil.readGerarchie();
        if (gerarchiaList != null) {
            for (Gerarchia gerarchia : gerarchiaList)
                System.out.println(gerarchia);
        } else
            System.out.println("Non sono presenti gerarchie.");
    }

    @Override
    public boolean execute() {
        this.visualizza();
        return false;
    }
}
