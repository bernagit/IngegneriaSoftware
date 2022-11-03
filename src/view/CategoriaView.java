package view;

import model.gerarchia.Categoria;

public class CategoriaView {
    Categoria categoria;
    public void print(Object obj){
        categoria = (Categoria) obj;
        System.out.println("\nSottocategoria di " + categoria.getPadre());
        System.out.println("Categoria: " + categoria.getNome());
        System.out.println("Descrizione: " + categoria.getDescrizione());
        System.out.println("Campi:");
        categoria.getCampi().forEach(System.out::println);
    }

}
