package model;

import java.io.FileWriter;
import java.io.IOException;

public class ReferralManager {
    private static ReferralManager instance;
    private final String logFilePath = "src/view/singleton_referral.txt";

    private ReferralManager() {}

    public static ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

//	Logging through singleton
    public void logAction(String action) {
        if (action == null) return;
        String lower = action.toLowerCase();
        if (lower.contains("ok") || lower.contains("cancel")) {
            return;
        }

        String timestamp = "[" + java.time.LocalDateTime.now().toString().replace("T", " ") + "]";
        String logLine = timestamp + " " + action;

        System.out.println(logLine);

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(logLine + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Logging the content
    public void logReferralContent(String[] referralData) {
        if (referralData == null || referralData.length == 0) return;

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write("\n=== Referral Entry ===\n");
            writer.write("Referral ID: " + referralData[0] + "\n");
            writer.write("Patient ID: " + referralData[1] + "\n");
            writer.write("Clinician ID: " + referralData[2] + "\n");
            writer.write("Referred To: " + referralData[3] + "\n");
            writer.write("Reason: " + referralData[4] + "\n");
            writer.write("Status: " + referralData[5] + "\n");
            writer.write("Notes: " + referralData[6] + "\n");
            writer.write("=======================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

