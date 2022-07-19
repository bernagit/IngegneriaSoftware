package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.gerarchia.Gerarchia;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtil {
    private final static String directoryGerarchie = "files/gerarchie/";

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
}