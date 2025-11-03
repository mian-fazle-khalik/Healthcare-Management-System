package model;
import java.io.*;
import java.util.*;

public class DataManager {
    public static List<String[]> loadCSV(String path) {
        List<String[]> rows = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) return rows;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(",", -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static void saveCSV(String path, List<String[]> data) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String[] r : data) {
                    bw.write(String.join(",", r));
                    bw.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
