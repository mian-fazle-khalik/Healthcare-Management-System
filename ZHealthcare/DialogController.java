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

    // ---------- Appointment dialogs ----------
    public void openAddAppointmentDialog() {
        String newId = generateSequentialId("A", currentData);
        JTextField idF = new JTextField(newId); idF.setEditable(false);
        JTextField patientIdF = new JTextField();
        JTextField clinicianIdF = new JTextField();
        JTextField facilityIdF = new JTextField();
        JTextField dateF = new JTextField(LocalDate.now().toString());
        JTextField timeF = new JTextField("09:00");
        JTextField durationF = new JTextField("30");
        JTextField typeF = new JTextField();
        JTextField statusF = new JTextField("Scheduled");
        JTextField reasonF = new JTextField();
        JTextField notesF = new JTextField();
        JTextField createdF = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        JTextField modifiedF = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Object[] fields = {
                "Appointment ID:", idF,
                "Patient ID:", patientIdF,
                "Clinician ID:", clinicianIdF,
                "Facility ID:", facilityIdF,
                "Date (yyyy-mm-dd):", dateF,
                "Time (HH:mm):", timeF,
                "Duration (minutes):", durationF,
                "Type:", typeF,
                "Status:", statusF,
                "Reason:", reasonF,
                "Notes:", notesF,
                "Created Date:", createdF,
                "Last Modified:", modifiedF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Add Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    idF.getText(), patientIdF.getText(), clinicianIdF.getText(), facilityIdF.getText(),
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

        JTextField idF = new JTextField(row[0]); idF.setEditable(false);
        JTextField patientIdF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField clinicianIdF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField facilityIdF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField dateF = new JTextField(row.length > 4 ? row[4] : "");
        JTextField timeF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField durationF = new JTextField(row.length > 6 ? row[6] : "");
        JTextField typeF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField statusF = new JTextField(row.length > 8 ? row[8] : "");
        JTextField reasonF = new JTextField(row.length > 9 ? row[9] : "");
        JTextField notesF = new JTextField(row.length > 10 ? row[10] : "");
        JTextField createdF = new JTextField(row.length > 11 ? row[11] : "");
        JTextField modifiedF = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Object[] fields = {
                "Appointment ID:", idF,
                "Patient ID:", patientIdF,
                "Clinician ID:", clinicianIdF,
                "Facility ID:", facilityIdF,
                "Date (yyyy-mm-dd):", dateF,
                "Time (HH:mm):", timeF,
                "Duration (minutes):", durationF,
                "Type:", typeF,
                "Status:", statusF,
                "Reason:", reasonF,
                "Notes:", notesF,
                "Created Date:", createdF,
                "Last Modified:", modifiedF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    idF.getText(), patientIdF.getText(), clinicianIdF.getText(), facilityIdF.getText(),
                    dateF.getText(), timeF.getText(), durationF.getText(), typeF.getText(),
                    statusF.getText(), reasonF.getText(), notesF.getText(), createdF.getText(), modifiedF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "appointments.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    // ---------- Prescription dialogs ----------
    public void openAddPrescriptionDialog() {
        String newId = generateSequentialId("PR", currentData);
        JTextField idF = new JTextField(newId); idF.setEditable(false);
        JTextField patientIdF = new JTextField();
        JTextField clinicianIdF = new JTextField();
        JTextField medicationF = new JTextField();
        JTextField dosageF = new JTextField();
        JTextField frequencyF = new JTextField();
        JTextField startDateF = new JTextField(LocalDate.now().toString());
        JTextField endDateF = new JTextField(LocalDate.now().plusDays(7).toString());
        JTextField notesF = new JTextField();

        Object[] fields = {
                "Prescription ID:", idF,
                "Patient ID:", patientIdF,
                "Clinician ID:", clinicianIdF,
                "Medication:", medicationF,
                "Dosage:", dosageF,
                "Frequency:", frequencyF,
                "Start Date:", startDateF,
                "End Date:", endDateF,
                "Notes:", notesF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Add Prescription", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    idF.getText(), patientIdF.getText(), clinicianIdF.getText(),
                    medicationF.getText(), dosageF.getText(), frequencyF.getText(),
                    startDateF.getText(), endDateF.getText(), notesF.getText()
            };
            currentData.add(newRow);
            DataManager.saveCSV(viewFolder + "prescriptions.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    public void openEditPrescriptionDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField idF = new JTextField(row[0]); idF.setEditable(false);
        JTextField patientIdF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField clinicianIdF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField medicationF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField dosageF = new JTextField(row.length > 4 ? row[4] : "");
        JTextField frequencyF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField startDateF = new JTextField(row.length > 6 ? row[6] : "");
        JTextField endDateF = new JTextField(row.length > 7 ? row[7] : "");
        JTextField notesF = new JTextField(row.length > 8 ? row[8] : "");

        Object[] fields = {
                "Prescription ID:", idF,
                "Patient ID:", patientIdF,
                "Clinician ID:", clinicianIdF,
                "Medication:", medicationF,
                "Dosage:", dosageF,
                "Frequency:", frequencyF,
                "Start Date:", startDateF,
                "End Date:", endDateF,
                "Notes:", notesF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Prescription", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    idF.getText(), patientIdF.getText(), clinicianIdF.getText(),
                    medicationF.getText(), dosageF.getText(), frequencyF.getText(),
                    startDateF.getText(), endDateF.getText(), notesF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "prescriptions.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    // ---------- Referral dialogs ----------
    public void openAddReferralDialog() {
        String newId = generateSequentialId("R", currentData);
        JTextField idF = new JTextField(newId); idF.setEditable(false);
        JTextField patientIdF = new JTextField();
        JTextField clinicianIdF = new JTextField();
        JTextField referredToF = new JTextField();
        JTextField reasonF = new JTextField();
        JTextField dateF = new JTextField(LocalDate.now().toString());
        JTextField notesF = new JTextField();

        Object[] fields = {
                "Referral ID:", idF,
                "Patient ID:", patientIdF,
                "Clinician ID:", clinicianIdF,
                "Referred To:", referredToF,
                "Reason:", reasonF,
                "Date:", dateF,
                "Notes:", notesF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Add Referral", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = {
                    idF.getText(), patientIdF.getText(), clinicianIdF.getText(),
                    referredToF.getText(), reasonF.getText(), dateF.getText(), notesF.getText()
            };
            currentData.add(newRow);
            ReferralManager.saveCSV(viewFolder + "referrals.csv", currentData);
            selectRowById(idF.getText());
        }
    }

    public void openEditReferralDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        String[] row = currentData.get(modelRow + 1);

        JTextField idF = new JTextField(row[0]); idF.setEditable(false);
        JTextField patientIdF = new JTextField(row.length > 1 ? row[1] : "");
        JTextField clinicianIdF = new JTextField(row.length > 2 ? row[2] : "");
        JTextField referredToF = new JTextField(row.length > 3 ? row[3] : "");
        JTextField reasonF = new JTextField(row.length > 4 ? row[4] : "");
        JTextField dateF = new JTextField(row.length > 5 ? row[5] : "");
        JTextField notesF = new JTextField(row.length > 6 ? row[6] : "");

        Object[] fields = {
                "Referral ID:", idF,
                "Patient ID:", patientIdF,
                "Clinician ID:", clinicianIdF,
                "Referred To:", referredToF,
                "Reason:", reasonF,
                "Date:", dateF,
                "Notes:", notesF
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Referral", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = {
                    idF.getText(), patientIdF.getText(), clinicianIdF.getText(),
                    referredToF.getText(), reasonF.getText(), dateF.getText(), notesF.getText()
            };
            currentData.set(modelRow + 1, updatedRow);
            ReferralManager.saveCSV(viewFolder + "referrals.csv", currentData);
            selectRowById(idF.getText());
        }
    }
    
    
 



    // ---------- Similarly, you can add methods ----------
    // openAddClinicianDialog(), openEditClinicianDialog()
    // openAddAppointmentDialog(), openEditAppointmentDialog()
    // openCreatePrescriptionDialog(), openCreateReferralDialog()
    // openAddStaffDialog(), openEditStaffDialog()
}
