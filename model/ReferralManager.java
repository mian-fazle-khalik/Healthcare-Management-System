//package model;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class ReferralManager {
//
//    private static ReferralManager instance;
//    private int counter = 1;
//
//    // ✅ CSV and log file paths
//    private final String csvFile = "src/view/referrals.csv";
//    private final String logFile = "src/view/referrals_output.txt";
//
//    // Private constructor for singleton
//    private ReferralManager() {
//        System.out.println("[ReferralManager] Singleton instance created.");
//    }
//
//    // Singleton getter
//    public static synchronized ReferralManager getInstance() {
//        if (instance == null) {
//            instance = new ReferralManager();
//        }
//        return instance;
//    }
//
//    public String generateReferralId() {
//        return "R" + (counter++);
//    }
//
//    public List<Referral> getAllReferrals() {
//        return DataManager.loadReferrals(csvFile);
//    }
//
//    public void addReferral(Referral referral) {
//        System.out.println("[DEBUG] addReferral() called.");
//
//        List<Referral> referrals = DataManager.loadReferrals(csvFile);
//        referrals.add(referral);
//        DataManager.saveReferrals(csvFile, referrals);
//
//        System.out.println("[DEBUG] Referral saved to CSV.");
//        logReferralAction(referral);
//    }
//
//    private void logReferralAction(Referral referral) {
//        StringBuilder log = new StringBuilder();
//        log.append("\n==============================================\n");
//        log.append("[ACTION] Referral button clicked\n");
//        log.append("[INFO] Entered the following details:\n");
//        log.append("Referral ID: ").append(referral.getReferralId()).append("\n");
//        log.append("Patient ID: ").append(referral.getPatientId()).append("\n");
//        log.append("Referring Clinician ID: ").append(referral.getReferringClinicianId()).append("\n");
//        log.append("Referred To Clinician ID: ").append(referral.getReferredToClinicianId()).append("\n");
//        log.append("Referral Reason: ").append(referral.getReferralReason()).append("\n");
//        log.append("Status: ").append(referral.getStatus()).append("\n");
//        log.append("Referral Date: ").append(referral.getReferralDate()).append("\n");
//        log.append("Notes: ").append(referral.getNotes()).append("\n");
//        log.append("[INFO] Referral added to list\n");
//        log.append("==============================================\n");
//
//        logAction(log.toString());
//    }
//
//    /**
//     * Logs any general message to the console **and** appends it to referrals_output.txt
//     */
//    public synchronized void logAction(String message) {
//        String timestamp = LocalDateTime.now()
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String logLine = "[" + timestamp + "] " + message + System.lineSeparator();
//
//        // Print to console
//        System.out.println(logLine);
//
//        // ✅ Append to file
//        try (FileWriter writer = new FileWriter(logFile, true)) { // true = append mode
//            writer.write(logLine);
//        } catch (IOException e) {
//            System.err.println("[ERROR] Failed to write log to file: " + logFile);
//            e.printStackTrace();
//        }
//    }
//
//    public static void saveCSV(String filePath, List<String[]> data) {
//        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
//            for (String[] row : data) {
//                writer.write(String.join(",", row));
//                writer.newLine();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateReferral(Referral updated) {
//        List<Referral> referrals = DataManager.loadReferrals(csvFile);
//        for (int i = 0; i < referrals.size(); i++) {
//            if (referrals.get(i).getReferralId().equals(updated.getReferralId())) {
//                referrals.set(i, updated);
//                break;
//            }
//        }
//        DataManager.saveReferrals(csvFile, referrals);
//        System.out.println("[DEBUG] Referral updated: " + updated.getReferralId());
//        logAction("[ACTION] Referral updated: " + updated.getReferralId());
//    }
//}

















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

    // ✅ Your existing logging method (unchanged)
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

    // ✅ New method: log actual referral content
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

