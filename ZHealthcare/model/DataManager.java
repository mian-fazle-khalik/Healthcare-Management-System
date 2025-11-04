package model;

import java.io.*;
import java.util.*;

/**
 * DataManager handles all CSV reading/writing for Patients, Clinicians,
 * Appointments, Prescriptions, Referrals, Facilities, and Staff.
 * 
 * Each load method skips the header row and converts each record
 * into its respective model object using the fromCSVRow() method.
 */
public class DataManager {

    // ===========================================================
    // BASIC CSV UTILITIES
    // ===========================================================

    public static List<String[]> loadCSV(String path) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split on commas (simple version — assume no quoted commas)
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
    // PATIENTS
    // ===========================================================

    public static List<Patient> loadPatients(String path) {
        List<Patient> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) { // skip header
            list.add(Patient.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void savePatients(String path, List<Patient> patients) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{
            "patient_id","first_name","last_name","date_of_birth","nhs_number",
            "gender","phone_number","email","address","postcode",
            "emergency_contact_name","emergency_contact_phone","registration_date","gp_surgery_id"
        });
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
        data.add(new String[]{
            "clinician_id","first_name","last_name","title","speciality","gmc_number",
            "phone_number","email","workplace_id","workplace_type","employment_status","start_date"
        });
        for (Clinician c : clinicians) data.add(c.toCSVRow());
        saveCSV(path, data);
    }

    // ===========================================================
    // FACILITIES
    // ===========================================================

    public static List<Facility> loadFacilities(String path) {
        List<Facility> list = new ArrayList<>();
        List<String[]> rows = loadCSV(path);
        for (int i = 1; i < rows.size(); i++) {
            list.add(Facility.fromCSVRow(rows.get(i)));
        }
        return list;
    }

    public static void saveFacilities(String path, List<Facility> facilities) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{
            "facility_id","facility_name","facility_type","address","postcode",
            "phone_number","email","opening_hours","manager_name","capacity","specialities_offered"
        });
        for (Facility f : facilities) data.add(f.toCSVRow());
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
        data.add(new String[]{
            "appointment_id","patient_id","clinician_id","facility_id","appointment_date",
            "appointment_time","duration_minutes","appointment_type","status","reason_for_visit",
            "notes","created_date","last_modified"
        });
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
        data.add(new String[]{
            "prescription_id","patient_id","clinician_id","appointment_id","prescription_date",
            "medication_name","dosage","frequency","duration_days","quantity",
            "instructions","pharmacy_name","status","issue_date","collection_date"
        });
        for (Prescription p : prescriptions) data.add(p.toCSVRow());
        saveCSV(path, data);
    }

    // ===========================================================
    // REFERRALS
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
            "referral_id","patient_id","referring_clinician_id","referred_to_clinician_id",
            "referring_facility_id","referred_to_facility_id","referral_date","urgency_level",
            "referral_reason","clinical_summary","requested_investigations","status",
            "appointment_id","notes","created_date","last_updated"
        });
        for (Referral r : referrals) data.add(r.toCSVRow());
        saveCSV(path, data);
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
        data.add(new String[]{
            "staff_id","first_name","last_name","role","department","facility_id",
            "phone_number","email","employment_status","start_date","line_manager","access_level"
        });
        for (Staff s : staffList) data.add(s.toCSVRow());
        saveCSV(path, data);
    }
}
