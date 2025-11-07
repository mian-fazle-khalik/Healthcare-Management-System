package model;

import java.io.*;
import java.util.*;

public class DataManager {

    // ===========================================================
    // BASIC CSV UTILS
    // ===========================================================

    public static List<String[]> loadCSV(String path) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + path);
            e.printStackTrace();
        }
        return data;
    }

    public static void saveCSV(String path, List<String[]> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + path);
            e.printStackTrace();
        }
    }

    // ===========================================================
    // GENERIC TEXT FILE EXPORTER
    // ===========================================================

    public static void saveTextFile(String filePath, List<String[]> data) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (String[] row : data) {
                    writer.println(String.join(", ", row));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing text file: " + filePath);
            e.printStackTrace();
        }
    }

    // ===========================================================
    // PATIENTS
    // ===========================================================

    public static List<Patient> loadPatients(String path) {
        List<Patient> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Patient.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void savePatients(String path, List<Patient> patients) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Name", "DOB", "Gender", "Contact"});
        for (Patient p : patients) data.add(p.toCSVRow());
        saveCSV(path, data);
    }

    // ===========================================================
    // CLINICIANS
    // ===========================================================

    public static List<Clinician> loadClinicians(String path) {
        List<Clinician> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Clinician.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void saveClinicians(String path, List<Clinician> clinicians) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Name", "Specialty", "Contact", "WorkingHours"});
        for (Clinician c : clinicians) data.add(c.toCSVRow());
        saveCSV(path, data);
    }

    // ===========================================================
    // APPOINTMENTS
    // ===========================================================

    public static List<Appointment> loadAppointments(String path) {
        List<Appointment> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Appointment.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void saveAppointments(String path, List<Appointment> appointments) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "PatientID", "ClinicianID", "Date", "Time", "Status"});
        for (Appointment a : appointments) data.add(a.toCSVRow());
        saveCSV(path, data);
    }

    // ===========================================================
    // PRESCRIPTIONS
    // ===========================================================

    public static List<Prescription> loadPrescriptions(String path) {
        List<Prescription> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Prescription.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void savePrescriptions(String path, List<Prescription> prescriptions) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "PatientID", "ClinicianID", "Medications", "Notes", "CreatedAt"});
        for (Prescription p : prescriptions) data.add(p.toCSVRow());
        saveCSV(path, data);

        // --- Optional text export ---
        saveTextFile("src/output/prescriptions.txt", data);
    }

    // ===========================================================
    // REFERRALS (Connected to ReferralManager)
    // ===========================================================

    public static List<Referral> loadReferrals(String path) {
        List<Referral> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Referral.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void saveReferrals(String path, List<Referral> referrals) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{
                "ReferralID", "PatientID", "RefClinicianID", "ReferredClinicianID", "RefFacilityID",
                "ReferredFacilityID", "Date", "Urgency", "Reason", "Summary",
                "Investigations", "Status", "AppointmentID", "Notes", "Created", "Updated"
        });

        for (Referral r : referrals) data.add(r.toCSVRow());
        saveCSV(path, data);

        // Export readable text
        saveTextFile("src/output/referrals.txt", data);
    }

    // ===========================================================
    // STAFF
    // ===========================================================

    public static List<Staff> loadStaff(String path) {
        List<Staff> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Staff.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void saveStaff(String path, List<Staff> staffList) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Name", "Role", "FacilityID", "Contact"});
        for (Staff s : staffList) data.add(s.toCSVRow());
        saveCSV(path, data);
    }
}
