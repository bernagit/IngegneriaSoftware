package db;

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

public class JsonManager {

    private static JsonManager instance = null;
    private final GsonBuilder builder;
    private final String directoryGerarchie;
    private final Path pathScambio;
    private final String directoryOfferte;
    private final String directoryBaratti;

    private JsonManager(){
        this.builder = new GsonBuilder();
        this.directoryGerarchie = "files/gerarchie/";
        this.pathScambio = Path.of("files/scambi/scambio.json");
        this.directoryOfferte = "files/offerte/";
        this.directoryBaratti = "files/baratti/";
    }
    public static JsonManager getInstance(){
        if(instance == null){
            instance = new JsonManager();
        }
        return instance;
    }

    public void writeGerarchia(Gerarchia gerarchia) {
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

    private List<Path> createListOfFile(String directory) {
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

    public List<Gerarchia> readGerarchie() {
        List<Gerarchia> gerarchiaList = new ArrayList<>();
        Gerarchia gerarchia;
        try {
            Reader reader;
            if (createListOfFile(directoryGerarchie) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryGerarchie)) {
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

    public boolean checkNomeGerarchiaRipetuto(String nome) {
        List<Gerarchia> gerarchiaList = readGerarchie();
        if (gerarchiaList != null) {
            for (Gerarchia g : gerarchiaList) {
                if (g.getNomeRadice().equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkGerarchiaExists(Path path) {
        List<Path> list = createListOfFile(directoryGerarchie);
        for (Path p : list) {
            if (p.getFileName().equals(path.getFileName())) {
                return true;
            }
        }
        return false;
    }

    public boolean scriviFileGerarchia(Path path, boolean overwrite) {
        try {
            if (!tryToReadGerarchia(path))
                return false;
            if (overwrite)
                Files.copy(path, Path.of(directoryGerarchie + path.getFileName() + ".old"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(path, Path.of(directoryGerarchie + path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean tryToReadGerarchia(Path pathFile) {
        Gson gson = new Gson();
        Gerarchia gerarchia;
        try {
            Reader reader = Files.newBufferedReader(pathFile);
            // convert JSON file to Gerarchia
            gerarchia = gson.fromJson(reader, Gerarchia.class);
        } catch (Exception e) {
            return false;
        }
        return gerarchia != null;
    }

    public Scambio readScambio() {
        Scambio scambio = null;
        try {
            Reader reader;
            reader = Files.newBufferedReader(pathScambio);
            Gson gson = new Gson();
            // convert JSON file to Gerarchia
            scambio = gson.fromJson(reader, Scambio.class);

        } catch (IOException ex) {
            System.out.println("Errore apertura file Scambi");
        }
        return scambio;
    }

    public void writeScambio(Scambio scambio) {
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

    public boolean checkScambioExists() {
        return Files.exists(pathScambio) && Files.isReadable(pathScambio);
    }

    public boolean scriviFileScambio(Path path, boolean overwrite) {
        try {
            if (!tryToReadScambio(path))
                return false;
            if (overwrite)
                Files.copy(pathScambio, Path.of(pathScambio.toString() + ".old"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(path, pathScambio, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean tryToReadScambio(Path pathFile) {
        Gson gson = new Gson();
        Scambio scambio = null;
        try {
            Reader reader = Files.newBufferedReader(pathFile);
            // convert JSON file to Gerarchia
            scambio = gson.fromJson(reader, Scambio.class);
        } catch (Exception e) {
            return false;
        }
        return scambio != null;
    }

    public void writeOfferta(Offerta offerta) {
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

    public List<Offerta> readOfferteByCategoriaAndState(String nomeCategoria, StatoOfferta stato) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryOfferte)) {
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

    public List<Offerta> readOffertaByAutore(String autore) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryOfferte)) {
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

    public List<Offerta> readOffertaByAutoreAndState(String autore, StatoOfferta stato) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryOfferte)) {
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

    public List<Offerta> readOfferteApertebyCategoria(String autore, Categoria categoria) {
        List<Offerta> offertaList = new ArrayList<>();
        Offerta offerta;
        try {
            Reader reader;
            if (createListOfFile(directoryOfferte) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryOfferte)) {
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

    public void writeBaratto(Baratto baratto) {
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

    public List<Baratto> readBarattoByUtente(String utenteB) {
        List<Baratto> barattoList = new ArrayList<>();
        Baratto baratto;
        try {
            Reader reader;
            if (createListOfFile(directoryBaratti) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryBaratti)) {
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

    public List<Baratto> readBarattoInScambio(String utente) {
        List<Baratto> barattoList = new ArrayList<>();
        Baratto baratto;
        try {
            Reader reader;
            if (createListOfFile(directoryBaratti) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryBaratti)) {
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

    public Baratto readBarattobyOfferta(Offerta offerta) {
        Baratto baratto;
        try {
            Reader reader;
            if (createListOfFile(directoryBaratti) == null) {
                return null;
            }
            for (Path file : createListOfFile(directoryBaratti)) {
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

    public void deleteBaratto(Baratto baratto) {
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

    public void eliminaBarattiScaduti() {
        Baratto baratto;
        LocalDateTime oggi = LocalDateTime.now();
        LocalDateTime scadenza;
        Scambio scambio = readScambio();
        try {
            Reader reader;
            if (createListOfFile(directoryBaratti) == null) {
                return;
            }
            for (Path file : createListOfFile(directoryBaratti)) {
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
                        writeOfferta(a);
                        writeOfferta(b);
                        deleteBaratto(baratto);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Baratti");
        }
    }
}
