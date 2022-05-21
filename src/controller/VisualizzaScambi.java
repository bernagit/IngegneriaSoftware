package controller;

import model.gerarchia.Gerarchia;
import model.scambio.IntervalloOrario;
import model.scambio.Scambio;
import model.user.Utente;
import utility.JsonUtil;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class VisualizzaScambi implements Action {
    @Override
    public Utente execute(Utente utente) throws ExitException {
        this.visualizzaScambio();
        return null;
    }

    private void visualizzaScambio() {
        //metodo per visualizzare gli scambi
        List<Gerarchia> gerarchie = JsonUtil.readGerarchie();
        StringBuilder stringBuilder = new StringBuilder();
        Scambio scambio = JsonUtil.readScambio();
        if (gerarchie != null){
            stringBuilder.append("Gerarchie:");
            for (Gerarchia gerarchia: gerarchie)
                stringBuilder.append("\nNome: ").append(gerarchia.getNomeRadice())
                        .append("\nDescrizione: ").append(gerarchia.getRadice().getDescrizione())
                        .append("\n\n");
            stringBuilder.append("\nPiazza di scambio: ").append(scambio.getPiazza())
                    .append("\nLuoghi:");
            for (String luogo: scambio.getLuoghi())
                stringBuilder.append("\n\t").append(luogo);
            stringBuilder.append("\nGiorni dello scambio:");
            for (DayOfWeek giorno: scambio.getGiorni())
                stringBuilder.append("\n\t").append(giorno.getDisplayName(TextStyle.FULL, Locale.getDefault()));
            stringBuilder.append("\nIntervalli orari:");
            for (IntervalloOrario interval: scambio.getIntervalliOrari()) {
                stringBuilder.append("\n\tOra inizio: ").append(interval.getOraInizio())
                        .append("\tOra fine: ").append(interval.getOraFine());
            }
            System.out.println(stringBuilder);
        }
        else
            System.out.println("miao");
    }
}
