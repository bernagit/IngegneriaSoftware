package utility;

import java.util.ArrayList;

/*
Questa classe rappresenta un menu testuale generico a piu' voci
Si suppone che la voce per uscire sia sempre associata alla scelta 0 
e sia presentata in fondo al menu

*/
public class MyMenu {
    final private static String CORNICE = "--------------------------------";
    //final private static String VOCE_USCITA = "0\tEsci";
    final private static String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";

    final private String titolo;
    private ArrayList<String> voci = new ArrayList<>();

    public MyMenu(String titolo) {
        this.titolo = titolo;
    }

    public void setVoci(ArrayList<String> voci){
        if(this.voci.equals(null))
            this.voci.clear();
        this.voci = voci;
    }
    public int scegli() {
        stampaMenu();
        return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.size());
    }

    private void stampaMenu() {
        System.out.println();
        System.out.println("--- " + titolo + " ----");
        for (int i = 0; i < voci.size(); i++) {
            System.out.println((i) + "\t" + voci.get(i));
        }
        System.out.println(CORNICE);
        System.out.println();
    }

}
