package controller;

import model.DataManager;
import model.ReferralManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DialogController {

    private final String viewFolder;
    private List<String[]> currentData;
    private JTable mainTable;

    public DialogController(String viewFolder, JTable mainTable, List<String[]> currentData) {
        this.viewFolder = viewFolder;
        this.mainTable = mainTable;
        this.currentData = currentData;
    }

    // ---------- Helper ----------
    private String generateSequentialId(String prefix, List<String[]> data) {
        if (data.size() <= 1) return prefix + "1";
        String lastId = data.get(data.size() - 1)[0];
        try {
            int num = Integer.parseInt(lastId.replace(prefix, ""));
            return prefix + (num + 1);
        } catch (Exception e) {
            return prefix + (data.size());
        }
    }

    private void selectRowById(String id) {
        if (mainTable == null || mainTable.getModel() == null) return;
        for (int i = 0; i < mainTable.getRowCount(); i++) {
            Object value = mainTable.getValueAt(i, 0);
            if (value != null && value.toString().equals(id)) {
                mainTable.setRowSelectionInterval(i, i);
                mainTable.scrollRectToVisible(mainTable.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private String getNameById(List<String[]> list, String id) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i)[0].equals(id)) {
                return list.get(i).length > 1 ? list.get(i)[1] + " " + (list.get(i).length > 2 ? list.get(i)[2] : "") : "";
            }
        }
        return "";
    }

    private void writeTextOutput(String filepath, String content) {
        try {
            File f = new File(filepath);
            f.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- Patient dialogs ----------
    public void openAddPatientDialog() {
        String newId = generateSequentialId("P", currentData);
        JTextField idF = new JTextField(newId); idF.setEditable(false);
        JTextField firstNameF = new JTextField();
        JTextField lastNameF = new JTextField();
        JTextField dobF = new JTextField(LocalDate.now().toString());
        JTextField nhsF = new JTextField();
        JTextField genderF = new JTextField();
        JTextField phoneF = new JTextField();
        JTextField emailF = new JTextField();
        JTextField addressF = new JTextField();
        JTextField postcodeF = new JTextField();
        JTextField emergencyNameF = new JTextField();
        JTextField emergencyPhoneF = new JTextField();
        JTextField registrationF = new JTextField(LocalDate.now().toString());
        JTextField gpSurgeryF = new JTextField();

        Object[] fields = {
                "Patient ID:", idF,
                "First Name:", firstNameF,
                "Last Name:", lastNameF,
                "Date of Birth (yyyy-mm-dd):", dobF,
                "NHS Number:", nhsF,
                "Gender:", genderF,
                "Phone Number:", phoneF,
                "Email:", emailF,
                "Address:", addressF,
                "Postcode:", postcodeF,
                "Emergency Contact Name:", emergencyNameF,
                "Emergency Contact Phone:", emergencyPhoneF,
                "Registration Date:", registrationF,
                "GP Surgery ID:", gpSurgeryF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Add New Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    idF.getText(), firstNameF.getText(), lastNameF.getText(), dobF.getText(), nhsF.getText(),
                    genderF.getText(), phoneF.getText(), emailF.getText(), addressF.getText(), postcodeF.getText(),
                    emergencyNameF.getText(), emergencyPhoneF.getText(), registrationF.getText(), gpSurgeryF.getText()
            };
            currentData.add(newRow);
            DataManager.saveCSV(viewFolder + "patients.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    public void openEditPatientDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField idF = new JTextField(row[0]); idF.setEditable(false);
        JTextField firstNameF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField lastNameF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField dobF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField nhsF = new JTextField(row.length > 4 ? row[4] : "");
        JTextField genderF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField phoneF = new JTextField(row.length > 6 ? row[6] : "");
        JTextField emailF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField addressF = new JTextField(row.length > 8 ? row[8] : "");
        JTextField postcodeF = new JTextField(row.length > 9 ? row[9] : "");
        JTextField emergencyNameF = new JTextField(row.length > 10 ? row[10] : "");
        JTextField emergencyPhoneF = new JTextField(row.length > 11 ? row[11] : "");
        JTextField registrationF = new JTextField(row.length > 12 ? row[12] : "");
        JTextField gpSurgeryF = new JTextField(row.length > 13 ? row[13] : "");

        Object[] fields = {
                "Patient ID:", idF,
                "First Name:", firstNameF,
                "Last Name:", lastNameF,
                "Date of Birth (yyyy-mm-dd):", dobF,
                "NHS Number:", nhsF,
                "Gender:", genderF,
                "Phone Number:", phoneF,
                "Email:", emailF,
                "Address:", addressF,
                "Postcode:", postcodeF,
                "Emergency Contact Name:", emergencyNameF,
                "Emergency Contact Phone:", emergencyPhoneF,
                "Registration Date:", registrationF,
                "GP Surgery ID:", gpSurgeryF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    idF.getText(), firstNameF.getText(), lastNameF.getText(), dobF.getText(), nhsF.getText(),
                    genderF.getText(), phoneF.getText(), emailF.getText(), addressF.getText(), postcodeF.getText(),
                    emergencyNameF.getText(), emergencyPhoneF.getText(), registrationF.getText(), gpSurgeryF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "patients.csv", currentData);
            selectRowById(idF.getText());
        }
    }
    
 // ---------- Clinician dialogs ----------
    public void openAddClinicianDialog() {
        String newId = generateSequentialId("C", currentData);
        JTextField idF = new JTextField(newId); idF.setEditable(false);
        JTextField firstNameF = new JTextField();
        JTextField lastNameF = new JTextField();
        JTextField titleF = new JTextField();
        JTextField specialityF = new JTextField();
        JTextField gmcF = new JTextField();
        JTextField phoneF = new JTextField();
        JTextField emailF = new JTextField();
        JTextField workplaceIdF = new JTextField();
        JTextField workplaceTypeF = new JTextField();
        JTextField employmentStatusF = new JTextField();
        JTextField startDateF = new JTextField(LocalDate.now().toString());

        Object[] fields = {
                "Clinician ID:", idF,
                "First Name:", firstNameF,
                "Last Name:", lastNameF,
                "Title:", titleF,
                "Speciality:", specialityF,
                "GMC Number:", gmcF,
                "Phone Number:", phoneF,
                "Email:", emailF,
                "Workplace ID:", workplaceIdF,
                "Workplace Type:", workplaceTypeF,
                "Employment Status:", employmentStatusF,
                "Start Date:", startDateF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Add Clinician", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    idF.getText(), firstNameF.getText(), lastNameF.getText(), titleF.getText(), specialityF.getText(),
                    gmcF.getText(), phoneF.getText(), emailF.getText(), workplaceIdF.getText(), workplaceTypeF.getText(),
                    employmentStatusF.getText(), startDateF.getText()
            };
            currentData.add(newRow);
            DataManager.saveCSV(viewFolder + "clinicians.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    public void openEditClinicianDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField idF = new JTextField(row[0]); idF.setEditable(false);
        JTextField firstNameF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField lastNameF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField titleF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField specialityF = new JTextField(row.length > 4 ? row[4] : "");
        JTextField gmcF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField phoneF = new JTextField(row.length > 6 ? row[6] : "");
        JTextField emailF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField workplaceIdF = new JTextField(row.length > 8 ? row[8] : "");
        JTextField workplaceTypeF = new JTextField(row.length > 9 ? row[9] : "");
        JTextField employmentStatusF = new JTextField(row.length > 10 ? row[10] : "");
        JTextField startDateF = new JTextField(row.length > 11 ? row[11] : "");

        Object[] fields = {
                "Clinician ID:", idF,
                "First Name:", firstNameF,
                "Last Name:", lastNameF,
                "Title:", titleF,
                "Speciality:", specialityF,
                "GMC Number:", gmcF,
                "Phone Number:", phoneF,
                "Email:", emailF,
                "Workplace ID:", workplaceIdF,
                "Workplace Type:", workplaceTypeF,
                "Employment Status:", employmentStatusF,
                "Start Date:", startDateF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Clinician", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    idF.getText(), firstNameF.getText(), lastNameF.getText(), titleF.getText(), specialityF.getText(),
                    gmcF.getText(), phoneF.getText(), emailF.getText(), workplaceIdF.getText(), workplaceTypeF.getText(),
                    employmentStatusF.getText(), startDateF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "clinicians.csv", currentData);
            selectRowById(idF.getText());
        }
    }

  
    
    
    public void openAddAppointmentDialog() {
        String newId = generateSequentialId("A", currentData);
        JTextField idF = new JTextField(newId); 
        idF.setEditable(false);

        // ----------------- Patient ID Dropdown -----------------
        List<String[]> patientsData = DataManager.loadCSV(viewFolder + "patients.csv");
        String[] patientOptions = patientsData.stream()
            .skip(1)
            .map(p -> p[0] + " - " + p[1] + " " + p[2])
            .toArray(String[]::new);
        JComboBox<String> patientIdCombo = new JComboBox<>(patientOptions);

        // ----------------- Clinician ID Dropdown -----------------
        List<String[]> cliniciansData = DataManager.loadCSV(viewFolder + "clinicians.csv");
        String[] clinicianOptions = cliniciansData.stream()
            .skip(1)
            .map(c -> c[0] + " - " + c[1] + " " + c[2])
            .toArray(String[]::new);
        JComboBox<String> clinicianIdCombo = new JComboBox<>(clinicianOptions);

        // ----------------- Facility ID Dropdown -----------------
        List<String[]> facilityData = DataManager.loadCSV(viewFolder + "facilities.csv");
        String[] facilityOptions = facilityData.stream()
            .skip(1)
            .map(f -> f[0] + " - " + f[1])
            .toArray(String[]::new);
        JComboBox<String> facilityIdCombo = new JComboBox<>(facilityOptions);

        // Other fields
        JTextField dateF = new JTextField(LocalDate.now().toString());
        JTextField timeF = new JTextField("09:00");
        JTextField durationF = new JTextField("30");
        JTextField typeF = new JTextField();
        JTextField statusF = new JTextField("Scheduled");
        JTextField reasonF = new JTextField();
        JTextField notesF = new JTextField();
        JTextField createdF = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        JTextField modifiedF = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Use a panel for better layout (scrollable)
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Appointment ID:")); panel.add(idF);
        panel.add(new JLabel("Patient:")); panel.add(patientIdCombo);
        panel.add(new JLabel("Clinician:")); panel.add(clinicianIdCombo);
        panel.add(new JLabel("Facility:")); panel.add(facilityIdCombo);
        panel.add(new JLabel("Date (yyyy-mm-dd):")); panel.add(dateF);
        panel.add(new JLabel("Time (HH:mm):")); panel.add(timeF);
        panel.add(new JLabel("Duration (minutes):")); panel.add(durationF);
        panel.add(new JLabel("Type:")); panel.add(typeF);
        panel.add(new JLabel("Status:")); panel.add(statusF);
        panel.add(new JLabel("Reason:")); panel.add(reasonF);
        panel.add(new JLabel("Notes:")); panel.add(notesF);
        panel.add(new JLabel("Created Date:")); panel.add(createdF);
        panel.add(new JLabel("Last Modified:")); panel.add(modifiedF);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Add Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Extract only the IDs from the selected dropdown values
            String patientId = ((String) patientIdCombo.getSelectedItem()).split(" - ")[0];
            String clinicianId = ((String) clinicianIdCombo.getSelectedItem()).split(" - ")[0];
            String facilityId = ((String) facilityIdCombo.getSelectedItem()).split(" - ")[0];

            String[] newRow = {
                idF.getText(), patientId, clinicianId, facilityId,
                dateF.getText(), timeF.getText(), durationF.getText(), typeF.getText(),
                statusF.getText(), reasonF.getText(), notesF.getText(), createdF.getText(), modifiedF.getText()
            };
            currentData.add(newRow);
            DataManager.saveCSV(viewFolder + "appointments.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    
    public void openEditAppointmentDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField idF = new JTextField(row[0]); 
        idF.setEditable(false);

        // ----------------- Patient ID Dropdown -----------------
        List<String[]> patientsData = DataManager.loadCSV(viewFolder + "patients.csv");
        String[] patientOptions = patientsData.stream()
            .skip(1)
            .map(p -> p[0] + " - " + p[1] + " " + p[2])
            .toArray(String[]::new);
        JComboBox<String> patientIdCombo = new JComboBox<>(patientOptions);
        // Select current value
        for (int i = 0; i < patientIdCombo.getItemCount(); i++) {
            if (patientIdCombo.getItemAt(i).startsWith(row.length > 1 ? row[1] : "")) {
                patientIdCombo.setSelectedIndex(i);
                break;
            }
        }

        // ----------------- Clinician ID Dropdown -----------------
        List<String[]> cliniciansData = DataManager.loadCSV(viewFolder + "clinicians.csv");
        String[] clinicianOptions = cliniciansData.stream()
            .skip(1)
            .map(c -> c[0] + " - " + c[1] + " " + c[2])
            .toArray(String[]::new);
        JComboBox<String> clinicianIdCombo = new JComboBox<>(clinicianOptions);
        for (int i = 0; i < clinicianIdCombo.getItemCount(); i++) {
            if (clinicianIdCombo.getItemAt(i).startsWith(row.length > 2 ? row[2] : "")) {
                clinicianIdCombo.setSelectedIndex(i);
                break;
            }
        }

        // ----------------- Facility ID Dropdown -----------------
        List<String[]> facilityData = DataManager.loadCSV(viewFolder + "facilities.csv");
        String[] facilityOptions = facilityData.stream()
            .skip(1)
            .map(f -> f[0] + " - " + f[1])
            .toArray(String[]::new);
        JComboBox<String> facilityIdCombo = new JComboBox<>(facilityOptions);
        for (int i = 0; i < facilityIdCombo.getItemCount(); i++) {
            if (facilityIdCombo.getItemAt(i).startsWith(row.length > 3 ? row[3] : "")) {
                facilityIdCombo.setSelectedIndex(i);
                break;
            }
        }

        // Other fields
        JTextField dateF = new JTextField(row.length > 4 ? row[4] : LocalDate.now().toString());
        JTextField timeF = new JTextField(row.length > 5 ? row[5] : "09:00");
        JTextField durationF = new JTextField(row.length > 6 ? row[6] : "30");
        JTextField typeF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField statusF = new JTextField(row.length > 8 ? row[8] : "Scheduled");
        JTextField reasonF = new JTextField(row.length > 9 ? row[9] : "");
        JTextField notesF = new JTextField(row.length > 10 ? row[10] : "");
        JTextField createdF = new JTextField(row.length > 11 ? row[11] : "");
        JTextField modifiedF = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Panel layout
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Appointment ID:")); panel.add(idF);
        panel.add(new JLabel("Patient:")); panel.add(patientIdCombo);
        panel.add(new JLabel("Clinician:")); panel.add(clinicianIdCombo);
        panel.add(new JLabel("Facility:")); panel.add(facilityIdCombo);
        panel.add(new JLabel("Date (yyyy-mm-dd):")); panel.add(dateF);
        panel.add(new JLabel("Time (HH:mm):")); panel.add(timeF);
        panel.add(new JLabel("Duration (minutes):")); panel.add(durationF);
        panel.add(new JLabel("Type:")); panel.add(typeF);
        panel.add(new JLabel("Status:")); panel.add(statusF);
        panel.add(new JLabel("Reason:")); panel.add(reasonF);
        panel.add(new JLabel("Notes:")); panel.add(notesF);
        panel.add(new JLabel("Created Date:")); panel.add(createdF);
        panel.add(new JLabel("Last Modified:")); panel.add(modifiedF);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String patientId = ((String) patientIdCombo.getSelectedItem()).split(" - ")[0];
            String clinicianId = ((String) clinicianIdCombo.getSelectedItem()).split(" - ")[0];
            String facilityId = ((String) facilityIdCombo.getSelectedItem()).split(" - ")[0];

            String[] updatedRow = {
                idF.getText(), patientId, clinicianId, facilityId,
                dateF.getText(), timeF.getText(), durationF.getText(), typeF.getText(),
                statusF.getText(), reasonF.getText(), notesF.getText(), createdF.getText(), modifiedF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "appointments.csv", currentData);
            selectRowById(idF.getText());
        }
    }


 // ---------- Prescription dialogs ----------
 // ---------- Add Prescription Dialog ----------
    public void openAddPrescriptionDialog() {
        String newId = generateSequentialId("PR", currentData);
        JTextField prescriptionIdF = new JTextField(newId); prescriptionIdF.setEditable(false);
        JTextField patientIdF = new JTextField();
        JTextField clinicianIdF = new JTextField();
        JTextField appointmentIdF = new JTextField();
        JTextField prescriptionDateF = new JTextField(LocalDate.now().toString());
        JTextField medicationNameF = new JTextField();
        JTextField dosageF = new JTextField();
        JTextField frequencyF = new JTextField();
        JTextField durationDaysF = new JTextField();
        JTextField quantityF = new JTextField();
        JTextField instructionsF = new JTextField();
        JTextField pharmacyNameF = new JTextField();
        JTextField statusF = new JTextField();
        JTextField issueDateF = new JTextField(LocalDate.now().toString());
        JTextField collectionDateF = new JTextField();

        // Create a panel to hold fields
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // 2 columns: label + field
        panel.add(new JLabel("Prescription ID:")); panel.add(prescriptionIdF);
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdF);
        panel.add(new JLabel("Clinician ID:")); panel.add(clinicianIdF);
        panel.add(new JLabel("Appointment ID:")); panel.add(appointmentIdF);
        panel.add(new JLabel("Prescription Date:")); panel.add(prescriptionDateF);
        panel.add(new JLabel("Medication Name:")); panel.add(medicationNameF);
        panel.add(new JLabel("Dosage:")); panel.add(dosageF);
        panel.add(new JLabel("Frequency:")); panel.add(frequencyF);
        panel.add(new JLabel("Duration (days):")); panel.add(durationDaysF);
        panel.add(new JLabel("Quantity:")); panel.add(quantityF);
        panel.add(new JLabel("Instructions:")); panel.add(instructionsF);
        panel.add(new JLabel("Pharmacy Name:")); panel.add(pharmacyNameF);
        panel.add(new JLabel("Status:")); panel.add(statusF);
        panel.add(new JLabel("Issue Date:")); panel.add(issueDateF);
        panel.add(new JLabel("Collection Date:")); panel.add(collectionDateF);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // adjust width/height

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Add Prescription", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    prescriptionIdF.getText(), patientIdF.getText(), clinicianIdF.getText(),
                    appointmentIdF.getText(), prescriptionDateF.getText(), medicationNameF.getText(),
                    dosageF.getText(), frequencyF.getText(), durationDaysF.getText(), quantityF.getText(),
                    instructionsF.getText(), pharmacyNameF.getText(), statusF.getText(),
                    issueDateF.getText(), collectionDateF.getText()
            };
            currentData.add(newRow);
            DataManager.saveCSV(viewFolder + "prescriptions.csv", currentData);
            selectRowById(prescriptionIdF.getText());
        }
    }

    // ---------- Edit Prescription Dialog ----------
    public void openEditPrescriptionDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField prescriptionIdF = new JTextField(row.length > 0 ? row[0] : ""); prescriptionIdF.setEditable(false);
        JTextField patientIdF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField clinicianIdF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField appointmentIdF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField prescriptionDateF = new JTextField(row.length > 4 ? row[4] : LocalDate.now().toString());
        JTextField medicationNameF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField dosageF = new JTextField(row.length > 6 ? row[6] : "");
        JTextField frequencyF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField durationDaysF = new JTextField(row.length > 8 ? row[8] : "");
        JTextField quantityF = new JTextField(row.length > 9 ? row[9] : "");
        JTextField instructionsF = new JTextField(row.length > 10 ? row[10] : "");
        JTextField pharmacyNameF = new JTextField(row.length > 11 ? row[11] : "");
        JTextField statusF = new JTextField(row.length > 12 ? row[12] : "");
        JTextField issueDateF = new JTextField(row.length > 13 ? row[13] : LocalDate.now().toString());
        JTextField collectionDateF = new JTextField(row.length > 14 ? row[14] : "");

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Prescription ID:")); panel.add(prescriptionIdF);
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdF);
        panel.add(new JLabel("Clinician ID:")); panel.add(clinicianIdF);
        panel.add(new JLabel("Appointment ID:")); panel.add(appointmentIdF);
        panel.add(new JLabel("Prescription Date:")); panel.add(prescriptionDateF);
        panel.add(new JLabel("Medication Name:")); panel.add(medicationNameF);
        panel.add(new JLabel("Dosage:")); panel.add(dosageF);
        panel.add(new JLabel("Frequency:")); panel.add(frequencyF);
        panel.add(new JLabel("Duration (days):")); panel.add(durationDaysF);
        panel.add(new JLabel("Quantity:")); panel.add(quantityF);
        panel.add(new JLabel("Instructions:")); panel.add(instructionsF);
        panel.add(new JLabel("Pharmacy Name:")); panel.add(pharmacyNameF);
        panel.add(new JLabel("Status:")); panel.add(statusF);
        panel.add(new JLabel("Issue Date:")); panel.add(issueDateF);
        panel.add(new JLabel("Collection Date:")); panel.add(collectionDateF);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Edit Prescription", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    prescriptionIdF.getText(), patientIdF.getText(), clinicianIdF.getText(),
                    appointmentIdF.getText(), prescriptionDateF.getText(), medicationNameF.getText(),
                    dosageF.getText(), frequencyF.getText(), durationDaysF.getText(), quantityF.getText(),
                    instructionsF.getText(), pharmacyNameF.getText(), statusF.getText(),
                    issueDateF.getText(), collectionDateF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "prescriptions.csv", currentData);
            selectRowById(prescriptionIdF.getText());
        }
    }

 // ---------- Add Referral Dialog ----------
    public void openAddReferralDialog() {
        String newId = generateSequentialId("R", currentData);

        JTextField referralIdF = new JTextField(newId); referralIdF.setEditable(false);
        JTextField patientIdF = new JTextField();
        JTextField referringClinicianIdF = new JTextField();
        JTextField referredToClinicianIdF = new JTextField();
        JTextField referringFacilityIdF = new JTextField();
        JTextField referredToFacilityIdF = new JTextField();
        JTextField referralDateF = new JTextField(LocalDate.now().toString());
        JTextField urgencyLevelF = new JTextField();
        JTextField referralReasonF = new JTextField();
        JTextField clinicalSummaryF = new JTextField();
        JTextField requestedInvestigationsF = new JTextField();
        JTextField statusF = new JTextField();
        JTextField appointmentIdF = new JTextField();
        JTextField notesF = new JTextField();
        JTextField createdDateF = new JTextField(LocalDateTime.now().toString()); createdDateF.setEditable(false);
        JTextField lastUpdatedF = new JTextField(LocalDateTime.now().toString()); lastUpdatedF.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Referral ID:")); panel.add(referralIdF);
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdF);
        panel.add(new JLabel("Referring Clinician ID:")); panel.add(referringClinicianIdF);
        panel.add(new JLabel("Referred To Clinician ID:")); panel.add(referredToClinicianIdF);
        panel.add(new JLabel("Referring Facility ID:")); panel.add(referringFacilityIdF);
        panel.add(new JLabel("Referred To Facility ID:")); panel.add(referredToFacilityIdF);
        panel.add(new JLabel("Referral Date:")); panel.add(referralDateF);
        panel.add(new JLabel("Urgency Level:")); panel.add(urgencyLevelF);
        panel.add(new JLabel("Referral Reason:")); panel.add(referralReasonF);
        panel.add(new JLabel("Clinical Summary:")); panel.add(clinicalSummaryF);
        panel.add(new JLabel("Requested Investigations:")); panel.add(requestedInvestigationsF);
        panel.add(new JLabel("Status:")); panel.add(statusF);
        panel.add(new JLabel("Appointment ID:")); panel.add(appointmentIdF);
        panel.add(new JLabel("Notes:")); panel.add(notesF);
        panel.add(new JLabel("Created Date:")); panel.add(createdDateF);
        panel.add(new JLabel("Last Updated:")); panel.add(lastUpdatedF);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Add Referral", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    referralIdF.getText(), patientIdF.getText(), referringClinicianIdF.getText(),
                    referredToClinicianIdF.getText(), referringFacilityIdF.getText(), referredToFacilityIdF.getText(),
                    referralDateF.getText(), urgencyLevelF.getText(), referralReasonF.getText(),
                    clinicalSummaryF.getText(), requestedInvestigationsF.getText(), statusF.getText(),
                    appointmentIdF.getText(), notesF.getText(), createdDateF.getText(), lastUpdatedF.getText()
            };
            currentData.add(newRow);
//            ReferralManager.saveCSV(viewFolder + "referrals.csv", currentData);
            DataManager.saveCSV(viewFolder + "referrals.csv", currentData);

            selectRowById(referralIdF.getText());
        }
    }

    // ---------- Edit Referral Dialog ----------
    public void openEditReferralDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField referralIdF = new JTextField(row.length > 0 ? row[0] : ""); referralIdF.setEditable(false);
        JTextField patientIdF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField referringClinicianIdF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField referredToClinicianIdF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField referringFacilityIdF = new JTextField(row.length > 4 ? row[4] : "");
        JTextField referredToFacilityIdF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField referralDateF = new JTextField(row.length > 6 ? row[6] : LocalDate.now().toString());
        JTextField urgencyLevelF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField referralReasonF = new JTextField(row.length > 8 ? row[8] : "");
        
        JTextArea clinicalSummaryF = new JTextArea(row.length > 9 ? row[9] : "");
        clinicalSummaryF.setRows(3); clinicalSummaryF.setLineWrap(true); clinicalSummaryF.setWrapStyleWord(true);
        
        JTextArea requestedInvestigationsF = new JTextArea(row.length > 10 ? row[10] : "");
        requestedInvestigationsF.setRows(3); requestedInvestigationsF.setLineWrap(true); requestedInvestigationsF.setWrapStyleWord(true);
        
        JTextField statusF = new JTextField(row.length > 11 ? row[11] : "");
        JTextField appointmentIdF = new JTextField(row.length > 12 ? row[12] : "");
        JTextField notesF = new JTextField(row.length > 13 ? row[13] : "");
        JTextField createdDateF = new JTextField(row.length > 14 ? row[14] : LocalDateTime.now().toString()); createdDateF.setEditable(false);
        JTextField lastUpdatedF = new JTextField(row.length > 15 ? row[15] : LocalDateTime.now().toString()); lastUpdatedF.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Referral ID:")); panel.add(referralIdF);
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdF);
        panel.add(new JLabel("Referring Clinician ID:")); panel.add(referringClinicianIdF);
        panel.add(new JLabel("Referred To Clinician ID:")); panel.add(referredToClinicianIdF);
        panel.add(new JLabel("Referring Facility ID:")); panel.add(referringFacilityIdF);
        panel.add(new JLabel("Referred To Facility ID:")); panel.add(referredToFacilityIdF);
        panel.add(new JLabel("Referral Date:")); panel.add(referralDateF);
        panel.add(new JLabel("Urgency Level:")); panel.add(urgencyLevelF);
        panel.add(new JLabel("Referral Reason:")); panel.add(referralReasonF);
        panel.add(new JLabel("Clinical Summary:")); panel.add(new JScrollPane(clinicalSummaryF));
        panel.add(new JLabel("Requested Investigations:")); panel.add(new JScrollPane(requestedInvestigationsF));
        panel.add(new JLabel("Status:")); panel.add(statusF);
        panel.add(new JLabel("Appointment ID:")); panel.add(appointmentIdF);
        panel.add(new JLabel("Notes:")); panel.add(notesF);
        panel.add(new JLabel("Created Date:")); panel.add(createdDateF);
        panel.add(new JLabel("Last Updated:")); panel.add(lastUpdatedF);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Edit Referral", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    referralIdF.getText(), patientIdF.getText(), referringClinicianIdF.getText(),
                    referredToClinicianIdF.getText(), referringFacilityIdF.getText(), referredToFacilityIdF.getText(),
                    referralDateF.getText(), urgencyLevelF.getText(), referralReasonF.getText(),
                    clinicalSummaryF.getText(), requestedInvestigationsF.getText(), statusF.getText(),
                    appointmentIdF.getText(), notesF.getText(), createdDateF.getText(), lastUpdatedF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
//            ReferralManager.saveCSV(viewFolder + "referrals.csv", currentData);
            DataManager.saveCSV(viewFolder + "referrals.csv", currentData);

            selectRowById(referralIdF.getText());
        }
    }
}
