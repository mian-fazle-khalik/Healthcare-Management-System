//package model;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.List;
//
//public class ReferralManager {
//    private static ReferralManager instance;
//    private int counter = 1;
//
//    private ReferralManager() {}
//
//    public static synchronized ReferralManager getInstance() {
//        if (instance == null) instance = new ReferralManager();
//        return instance;
//    }
//
//    public String generateReferralId() {
//        return "R" + (counter++);
//    }
//
//    // --- Add this method ---
//    public static void saveCSV(String filepath, List<String[]> data) {
//        try {
//            File f = new File(filepath);
//            f.getParentFile().mkdirs();
//            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
//                for (String[] row : data) {
//                    bw.write(String.join(",", row));
//                    bw.newLine();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}







package model;

import java.util.List;

public class ReferralManager {

    private static ReferralManager instance;
    private int counter = 1;

    private final String filePath = "src/view/referrals.csv";

    // Private constructor for singleton
    private ReferralManager() {}

    // Singleton getter
    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    public String generateReferralId() {
        return "R" + (counter++);
    }

    public List<Referral> getAllReferrals() {
        return DataManager.loadReferrals(filePath);
    }

    public void addReferral(Referral referral) {
        System.out.println("[DEBUG] addReferral() called.");

        List<Referral> referrals = DataManager.loadReferrals(filePath);
        referrals.add(referral);
        DataManager.saveReferrals(filePath, referrals);

        System.out.println("[DEBUG] Referral saved to CSV.");

        logReferralAction(referral);
    }

    private void logReferralAction(Referral referral) {
        System.out.println("==============================================");
        System.out.println("Referral button clicked");
        System.out.println("Entered the following details:");
        System.out.println("Referral ID: " + referral.getReferralId());
        System.out.println("Patient ID: " + referral.getPatientId());
        System.out.println("Referring Clinician ID: " + referral.getReferringClinicianId());
        System.out.println("Referred To Clinician ID: " + referral.getReferredToClinicianId());
        System.out.println("Referral Reason: " + referral.getReferralReason());
        System.out.println("Status: " + referral.getStatus());
        System.out.println("Referral Date: " + referral.getReferralDate());
        System.out.println("Notes: " + referral.getNotes());
        System.out.println("Referral added to list");
        System.out.println("==============================================");
    }

    public static void saveCSV(String filePath, List<String[]> data) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateReferral(Referral updated) {
        List<Referral> referrals = DataManager.loadReferrals(filePath);
        for (int i = 0; i < referrals.size(); i++) {
            if (referrals.get(i).getReferralId().equals(updated.getReferralId())) {
                referrals.set(i, updated);
                break;
            }
        }
        DataManager.saveReferrals(filePath, referrals);
        System.out.println("[DEBUG] Referral updated: " + updated.getReferralId());
    }
}
