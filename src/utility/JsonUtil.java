package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.structure.Gerarchia;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtil {
    private final static String directory = "files/gerarchie/";
    private final static String dataName = "files/gerarchie/gerarchia";

    private static long getNumFile() {
        long count = 0;
        try (Stream<Path> files = Files.list(Paths.get(directory))) {
            count = files.count();
            System.out.println(count);
        } catch (IOException e) {
            System.out.println("Errore di sto cazzo");
        }
        return count;
    }

    public static void writeGerarchia(Gerarchia gerarchia) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        long numFile = JsonUtil.getNumFile();
        String nomeFile = dataName + numFile + ".json";
        try (
                FileWriter writer = new FileWriter(nomeFile)
        ) {
            writer.write(gson.toJson(gerarchia));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio della gerarchia");
        }

    }

    private static List<Path> createListOfFile() {
        List<Path> list = null;
        try (Stream<Path> files = Files.list(Paths.get(directory))) {
            list = files.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Errore di sto cazzo");
        }
        return list;
    }

    public static List<Gerarchia> readGerarchie() {
        List<Gerarchia> gerarchiaList = new ArrayList<>();
        Gerarchia gerarchia;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile() == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile()) {
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
        for (Gerarchia g : gerarchiaList) {
            if (g.getCategoriaRadice().getNome().equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkNomeCategoriaRipetuto(String nome) {
        List<Gerarchia> gerarchiaList = JsonUtil.readGerarchie();
        for (Gerarchia g : gerarchiaList) {
            return g.nomeRipetuto(nome);
        }
        return false;
    }
}