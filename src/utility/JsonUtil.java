package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.baratto.Baratto;
import model.gerarchia.Categoria;
import model.gerarchia.Gerarchia;
import model.offerta.Offerta;
import model.offerta.StatoOfferta;
import model.scambio.Scambio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtil {
    private final static String directoryGerarchie = "files/gerarchie/";
    private final static String directoryScambi = "files/scambi/";
    private final static String directoryOfferte = "files/offerte/";
    private final static String directoryBaratti = "files/baratti/";

    public static void writeGerarchia(Gerarchia gerarchia) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String nomeFile = directoryGerarchie + gerarchia.getNomeRadice() + ".json";
        try (
                FileWriter writer = new FileWriter(nomeFile)
        ) {
            writer.write(gson.toJson(gerarchia));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio della gerarchia");
        }
    }

    private static List<Path> createListOfFile(String directory) {
        List<Path> list = null;

        try (Stream<Path> files = Files.list(Paths.get(directory))) {
            list = files.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Errore: directory files/gerarchie non presente");
        }
        return list;
    }

    public static List<Gerarchia> readGerarchie() {
        List<Gerarchia> gerarchiaList = new ArrayList<>();
        Gerarchia gerarchia;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryGerarchie) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryGerarchie)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                gerarchia = gson.fromJson(reader, Gerarchia.class);
                gerarchiaList.add(gerarchia);
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Gerarchie");
        }
        return gerarchiaList;
    }

    public static boolean checkNomeGerarchiaRipetuto(String nome) {
        List<Gerarchia> gerarchiaList = JsonUtil.readGerarchie();
        if (gerarchiaList != null) {
            for (Gerarchia g : gerarchiaList) {
                if (g.getNomeRadice().equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Scambio readScambio() {
        Scambio scambio = null;
        try {
            Reader reader;
            reader = Files.newBufferedReader(Path.of(directoryScambi + "scambio.json"));
            Gson gson = new Gson();
            // convert JSON file to Gerarchia
            scambio = gson.fromJson(reader, Scambio.class);

        } catch (
                IOException ex) {
            System.out.println("Errore apertura file Scambi");
        }
        return scambio;
    }


    public static void writeScambio(Scambio scambio) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String nomeFile = directoryScambi + "scambio.json";


        try (
                FileWriter writer = new FileWriter(nomeFile)
        ) {
            writer.write(gson.toJson(scambio));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dello scambio");
        }
    }

    public static void writeOfferta(Offerta offerta) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        StringBuilder nomeFile = new StringBuilder();
        nomeFile.append(directoryOfferte).append(offerta.getTitolo())
                .append("_").append(offerta.getAutore())
                .append(".json");
        try (
                FileWriter writer = new FileWriter(nomeFile.toString())
        ) {
            writer.write(gson.toJson(offerta));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dell' offerta");
        }
    }

    public static List<Offerta> readOfferteByCategoriaAndState(String nomeCategoria, StatoOfferta stato) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryOfferte)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                offerta = gson.fromJson(reader, Offerta.class);
                if (nomeCategoria.equals(offerta.getCategoriaName()) && offerta.getStatoCorrente().equals(stato))
                    offertaList.add(offerta);
            }
        } catch (Exception ex) {
            System.out.println("Errore apertura file Offerte");
        }
        return offertaList;
    }

    public static List<Offerta> readOffertaByAutore(String autore) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryOfferte)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                offerta = gson.fromJson(reader, Offerta.class);
                if (autore.equals(offerta.getAutore()))
                    offertaList.add(offerta);
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Offerte");
        }
        return offertaList;
    }

    public static List<Offerta> readOffertaByAutoreAndState(String autore, StatoOfferta stato) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryOfferte)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                offerta = gson.fromJson(reader, Offerta.class);
                if (autore.equals(offerta.getAutore()) && stato.equals(offerta.getStatoCorrente()))
                    offertaList.add(offerta);
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Offerte");
        }
        return offertaList;
    }

    public static List<Offerta> readOfferteApertebyCategoria(String autore, Categoria categoria) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryOfferte)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                offerta = gson.fromJson(reader, Offerta.class);
                if (!autore.equals(offerta.getAutore())) {
                    if (offerta.getStatoCorrente().equals(StatoOfferta.APERTA)
                            && offerta.getCategoria().getNome().equals(categoria.getNome())
                            && offerta.getCategoria().getPadre().equals(categoria.getPadre())) {
                        offertaList.add(offerta);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Offerte");
        }
        return offertaList;
    }

    public static void writeBaratto(Baratto baratto) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        StringBuilder nomeFile = new StringBuilder();
        nomeFile.append(directoryBaratti)
                .append(baratto.getOffertaA().getAutore()).append("_")
                .append(baratto.getOffertaB().getAutore()).append("_")
                .append("_").append(baratto.getDataOraBaratto())
                .append(".json");
        try (
                FileWriter writer = new FileWriter(nomeFile.toString())
        ) {
            writer.write(gson.toJson(baratto));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio del baratto");
        }
    }

    public static List<Baratto> readBarattoByUtente(String utenteB) {
        List<Baratto> barattoList = new ArrayList<>();
        Baratto baratto;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryBaratti) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryBaratti)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                baratto = gson.fromJson(reader, Baratto.class);
                if (baratto.getUtenteB().equals(utenteB) && baratto.getOffertaB().getStatoCorrente().equals(StatoOfferta.SELEZIONATA)) {
                    barattoList.add(baratto);
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Baratti");
        }
        return barattoList;
    }

    public static List<Baratto> readBarattoInScambio(String utente) {
        List<Baratto> barattoList = new ArrayList<>();
        Baratto baratto;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryBaratti) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryBaratti)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                baratto = gson.fromJson(reader, Baratto.class);
                if (baratto.getUtenteB().equals(utente) || baratto.getUtenteA().equals(utente)) {
                    if (baratto.getOffertaB().getStatoCorrente().equals(StatoOfferta.IN_SCAMBIO)
                            && baratto.getOffertaB().getStatoCorrente().equals(StatoOfferta.IN_SCAMBIO)) {
                        barattoList.add(baratto);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Baratti");
        }
        return barattoList;
    }

    public static Baratto readBarattobyOfferta(Offerta offerta) {
        Baratto baratto;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryBaratti) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryBaratti)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                baratto = gson.fromJson(reader, Baratto.class);
                if (baratto.getOffertaA().equals(offerta) || baratto.getOffertaB().equals(offerta)) {
                    return baratto;
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Baratti");
        }
        return null;
    }

    public static void deleteBaratto(Baratto baratto) {
        StringBuilder nomeFile = new StringBuilder();
        nomeFile.append(directoryBaratti)
                .append(baratto.getOffertaA().getAutore()).append("_")
                .append(baratto.getOffertaB().getAutore()).append("_")
                .append("_").append(baratto.getDataOraBaratto())
                .append(".json");

        File fileBaratto = new File(nomeFile.toString());
        if (fileBaratto.delete())
            System.out.println("Baratto chiuso");
        else
            System.out.println("impossibile chiudere baratto");
    }

    public static boolean eliminaBarattiScaduti() {
        Baratto baratto;
        LocalDateTime oggi = LocalDateTime.now();
        LocalDateTime scadenza;
        Scambio scambio = JsonUtil.readScambio();
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryBaratti) == null) {
                return false;
            }
            for (Path file : JsonUtil.createListOfFile(directoryBaratti)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                baratto = gson.fromJson(reader, Baratto.class);
                if (baratto.getOffertaA().getStatoCorrente().equals(StatoOfferta.ACCOPPIATA)) {
                    scadenza = baratto.getDataOraBaratto().plusDays(scambio.getScadenzaProposta());
                    if (scadenza.isBefore(oggi)){
                        //cambio stati
                        Offerta a = baratto.getOffertaA();
                        a.setStatoCorrente(StatoOfferta.APERTA);
                        Offerta b = baratto.getOffertaB();
                        b.setStatoCorrente(StatoOfferta.APERTA);
                        JsonUtil.writeOfferta(a);
                        JsonUtil.writeOfferta(b);

                        JsonUtil.deleteBaratto(baratto);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Baratti");
        }
        return true;
    }

}
