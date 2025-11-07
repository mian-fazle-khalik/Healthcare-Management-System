//package model;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.List;
//
//public class PrescriptionManager {
//
//    private static final String PRESCRIPTION_FILE = "src/view/prescriptions.csv";
//    private static final String OUTPUT_FILE = "src/view/referrals_output.txt"; // same file if you want combined log
//
//    public static void addPrescription(String[] newPrescription) {
//        List<String[]> allPrescriptions = DataManager.loadData(PRESCRIPTION_FILE, true);
//        allPrescriptions.add(newPrescription);
//        DataManager.saveData(PRESCRIPTION_FILE, allPrescriptions, true);
//
//        // Log to text file
//        logPrescriptionToTextFile(newPrescription);
//    }
//
//    private static void logPrescriptionToTextFile(String[] prescription) {
//        try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
//            writer.write("\n=== New Prescription Added ===\n");
//            writer.write("Prescription ID: " + prescription[0] + "\n");
//            writer.write("Patient ID: " + prescription[1] + "\n");
//            writer.write("Clinician ID: " + prescription[2] + "\n");
//            writer.write("Medication: " + prescription[3] + "\n");
//            writer.write("Dosage: " + prescription[4] + "\n");
//            writer.write("Start Date: " + prescription[5] + "\n");
//            writer.write("End Date: " + prescription[6] + "\n");
//            writer.write("Notes: " + prescription[7] + "\n");
//            writer.write("====================================\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
