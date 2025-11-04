package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReferralManager {
    private static ReferralManager instance;
    private int counter = 1;

    private ReferralManager() {}

    public static synchronized ReferralManager getInstance() {
        if (instance == null) instance = new ReferralManager();
        return instance;
    }

    public String generateReferralId() {
        return "R" + (counter++);
    }

    // --- Add this method ---
    public static void saveCSV(String filepath, List<String[]> data) {
        try {
            File f = new File(filepath);
            f.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                for (String[] row : data) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
