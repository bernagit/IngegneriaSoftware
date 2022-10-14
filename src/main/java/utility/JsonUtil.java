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
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtil {
    private final static String directoryGerarchie = "files/gerarchie/";
    private final static Path pathScambio = Path.of("files/scambi/scambio.json");
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
            list = files.map(Path::toString)
                    .filter(e -> e.endsWith(".json"))
                    .map(Path::of)
                    .collect(Collectors.toList());
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

    public static boolean checkGerarchiaExists(Path path) {
        List<Path> list = createListOfFile(directoryGerarchie);
        for (Path p : list) {
            if (p.getFileName().equals(path.getFileName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean scriviFileGerarchia(Path path, boolean overwrite) {
        try {
            Path pathFile = Path.of(directoryGerarchie + path.getFileName());
            if (!JsonUtil.tryToReadGerarchia(pathFile))
                return false;
            if (overwrite)
                Files.copy(pathFile, Path.of(pathFile + ".old"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(path, pathFile, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean tryToReadGerarchia(Path pathFile) {
        Gson gson = new Gson();
        Gerarchia gerarchia;
        try {
            Reader reader = Files.newBufferedReader(pathFile);
            // convert JSON file to Gerarchia
            gerarchia = gson.fromJson(reader, Gerarchia.class);
        } catch (Exception e) {
            return false;
        }
        return gerarchia == null;
    }

    public static Scambio readScambio() {
        Scambio scambio = null;
        try {
            Reader reader;
            reader = Files.newBufferedReader(pathScambio);
            Gson gson = new Gson();
            // convert JSON file to Gerarchia
            scambio = gson.fromJson(reader, Scambio.class);

        } catch (IOException ex) {
            //System.out.println("Errore apertura file Scambi");
        }
        return scambio;
    }

    public static void writeScambio(Scambio scambio) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (
                FileWriter writer = new FileWriter(pathScambio.toString())
        ) {
            writer.write(gson.toJson(scambio));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dello scambio");
        }
    }

    public static boolean checkScambioExists() {
        return Files.exists(pathScambio) && Files.isReadable(pathScambio);
    }

    public static boolean scriviFileScambio(Path path, boolean overwrite) {
        try {
            if (!JsonUtil.tryToReadScambio(path))
                return false;
            if (overwrite)
                Files.copy(pathScambio, Path.of(pathScambio + ".old"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(path, pathScambio, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean tryToReadScambio(Path pathFile) {
        Gson gson = new Gson();
        Scambio scambio;
        try {
            Reader reader = Files.newBufferedReader(pathFile);
            // convert JSON file to Gerarchia
            scambio = gson.fromJson(reader, Scambio.class);
        } catch (Exception e) {
            return false;
        }
        return scambio == null;
    }

    public static void writeOfferta(Offerta offerta) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        var nomeFile = directoryOfferte + offerta.getTitolo() +
                "_" + offerta.getAutore() +
                ".json";
        try (
                FileWriter writer = new FileWriter(nomeFile)
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
        var nomeFile = directoryBaratti +
                baratto.getOffertaA().getAutore() + "_" +
                baratto.getOffertaB().getAutore() + "_" +
                "_" + baratto.getDataOraBaratto() +
                ".json";
        try (
                FileWriter writer = new FileWriter(nomeFile)
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
        String nomeFile = directoryBaratti +
                baratto.getOffertaA().getAutore() + "_" +
                baratto.getOffertaB().getAutore() + "_" +
                "_" + baratto.getDataOraBaratto() +
                ".json";

        File fileBaratto = new File(nomeFile);
        if (fileBaratto.delete())
            System.out.println("Baratto chiuso");
        else
            System.out.println("impossibile chiudere baratto");
    }

    public static void eliminaBarattiScaduti() {
        Baratto baratto;
        LocalDateTime oggi = LocalDateTime.now();
        LocalDateTime scadenza;
        Scambio scambio = JsonUtil.readScambio();
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryBaratti) == null) {
                return;
            }
            for (Path file : JsonUtil.createListOfFile(directoryBaratti)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                baratto = gson.fromJson(reader, Baratto.class);
                if (baratto.getOffertaA().getStatoCorrente().equals(StatoOfferta.ACCOPPIATA)
                        || baratto.getOffertaA().getStatoCorrente().equals(StatoOfferta.IN_SCAMBIO)) {
                    scadenza = baratto.getDataOraBaratto().plusDays(scambio.getScadenzaProposta());
                    if (scadenza.isBefore(oggi)) {
                        //cambio stati offerta
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
    }
}
