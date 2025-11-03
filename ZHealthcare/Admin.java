// put this in controller/Admin.java or adapt your existing file
package controller;

import model.DataManager;
import model.ReferralManager; // singleton
import model.Patient; // basic POJO if you choose to create model objects
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Admin extends JFrame {
    private JPanel navPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;
    private JTable mainTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private String currentEntity = "patients"; // which CSV is displayed
    private final String viewFolder = "src/view/"; // csv files location
    private List<String[]> currentData = new ArrayList<>();

    public Admin() {
        setTitle("Healthcare Admin Dashboard");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Build UI
        buildNavPanel();
        buildCenterPanel();
        buildRightPanel();

        JSplitPane centerRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPanel, rightPanel);
        centerRight.setDividerLocation(700);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, centerRight);
        mainSplit.setDividerLocation(180);

        add(mainSplit, BorderLayout.CENTER);

        // initial view: patients
        showEntity("patients");
    }

    private void buildNavPanel() {
        navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(0, 1, 5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] entities = {"patients", "clinicians", "appointments", "prescriptions", "referrals", "staff", "facilities"};
        for (String ent : entities) {
            JButton b = new JButton(ent.substring(0,1).toUpperCase() + ent.substring(1));
            b.addActionListener(e -> showEntity(ent));
            navPanel.add(b);
        }
    }

    private void buildCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());

        // toolbar with Add/Edit/Delete/Refresh
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> handleAdd());
        editBtn.addActionListener(e -> handleEdit());
        delBtn.addActionListener(e -> handleDelete());
        refreshBtn.addActionListener(e -> refreshCurrent());

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(delBtn);
        toolbar.add(refreshBtn);

        centerPanel.add(toolbar, BorderLayout.NORTH);

        // empty table initially
        tableModel = new DefaultTableModel();
        mainTable = new JTable(tableModel);
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // when row selected, update right panel
        mainTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateRightPanelSelection();
            }
        });

        tableScroll = new JScrollPane(mainTable);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
    }

    private JPanel createDetailsPanel(Map<String, String> details) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 2)); // 2px vertical gap
        for (Map.Entry<String, String> entry : details.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // no padding
            panel.add(label);
        }
        return panel;
    }

    private void buildRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Details / Actions"));

        // default content
        JPanel defaultContent = new JPanel(new BorderLayout());
        defaultContent.add(new JLabel("Select a record from the table."), BorderLayout.NORTH);
        rightPanel.add(defaultContent, BorderLayout.CENTER);
    }

    private void showEntity(String entity) {
        currentEntity = entity;
        loadCSVToTable(entity + ".csv");
    }

    private void loadCSVToTable(String filename) {
        String path = viewFolder + filename;
        currentData = DataManager.loadCSV(path); // returns List<String[]>
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            if (currentData.size() == 0) {
                tableModel.addColumn("Empty");
                return;
            }
            String[] header = currentData.get(0);
            for (String h : header) tableModel.addColumn(h);
            for (int i = 1; i < currentData.size(); i++) {
                tableModel.addRow(currentData.get(i));
            }
        });
    }

    private void refreshCurrent() {
        showEntity(currentEntity);
    }

    // ---------- CRUD handlers ----------
    private void handleAdd() {
        switch (currentEntity) {
            case "patients":
                openAddPatientDialog();
                break;
            // implement for other entities similarly
            default:
                JOptionPane.showMessageDialog(this, "Add is not implemented for " + currentEntity);
        }
    }

    private void handleEdit() {
        int sel = mainTable.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
        switch (currentEntity) {
            case "patients":
                openEditPatientDialog(sel);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Edit is not implemented for " + currentEntity);
        }
    }

    private void handleDelete() {
        int sel = mainTable.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
        int modelRow = mainTable.convertRowIndexToModel(sel);
        if (JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // remember currentData includes header at index 0
            currentData.remove(modelRow + 1);
            DataManager.saveCSV(viewFolder + currentEntity + ".csv", currentData);
            refreshCurrent();
        }
    }

    // ---------- Patients dialogs ----------
    private void openAddPatientDialog() {
        JTextField idF = new JTextField(generateId("P", currentData));
        idF.setEditable(false);
        JTextField nameF = new JTextField();
        JTextField dobF = new JTextField(); // yyyy-MM-dd
        JTextField genderF = new JTextField();
        JTextField contactF = new JTextField();

        Object[] fields = {
            "ID:", idF,
            "Name:", nameF,
            "DOB (yyyy-mm-dd):", dobF,
            "Gender:", genderF,
            "Contact:", contactF
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] row = new String[] { idF.getText(), nameF.getText(), dobF.getText(), genderF.getText(), contactF.getText() };
            // append to currentData then save
            currentData.add(row);
            DataManager.saveCSV(viewFolder + "patients.csv", currentData);
            refreshCurrent();
            selectRowById(idF.getText());
        }
    }

    private void openEditPatientDialog(int selectedRow) {
        int modelRow = mainTable.convertRowIndexToModel(selectedRow);
        // modelRow corresponds to index in currentData starting at 1
        String[] row = currentData.get(modelRow + 1);
        JTextField idF = new JTextField(row[0]); idF.setEditable(false);
        JTextField nameF = new JTextField(row[1]);
        JTextField dobF = new JTextField(row.length>2?row[2]:"");
        JTextField genderF = new JTextField(row.length>3?row[3]:"");
        JTextField contactF = new JTextField(row.length>4?row[4]:"");

        Object[] fields = {
            "ID:", idF,
            "Name:", nameF,
            "DOB (yyyy-mm-dd):", dobF,
            "Gender:", genderF,
            "Contact:", contactF
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = new String[] { idF.getText(), nameF.getText(), dobF.getText(), genderF.getText(), contactF.getText() };
            currentData.set(modelRow + 1, newRow);
            DataManager.saveCSV(viewFolder + "patients.csv", currentData);
            refreshCurrent();
            selectRowById(idF.getText());
        }
    }

    private void selectRowById(String id) {
        // find in table and select
        for (int r=0; r<mainTable.getRowCount(); r++) {
            if (mainTable.getValueAt(r, 0).toString().equals(id)) {
                mainTable.getSelectionModel().setSelectionInterval(r, r);
                mainTable.scrollRectToVisible(mainTable.getCellRect(r, 0, true));
                break;
            }
        }
    }

    private String generateId(String prefix, List<String[]> data) {
        // naive generation: prefix + (timestamp)
        return prefix + System.currentTimeMillis();
    }

    // updates right panel when selection changes
    private void updateRightPanelSelection() {
        int sel = mainTable.getSelectedRow();
        rightPanel.removeAll();
        if (sel < 0) {
            rightPanel.add(new JLabel("Select a record."), BorderLayout.NORTH);
            rightPanel.revalidate();
            rightPanel.repaint();
            return;
        }
        int modelRow = mainTable.convertRowIndexToModel(sel);
        String[] rowData = currentData.get(modelRow + 1);
        JPanel info = new JPanel(new GridLayout(0,1));
        for (int i=0; i<Math.min(rowData.length, tableModel.getColumnCount()); i++) {
            info.add(new JLabel(tableModel.getColumnName(i) + ": " + rowData[i]));
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (currentEntity.equals("patients")) {
            JButton bookApp = new JButton("Book Appointment");
            JButton newPres = new JButton("Create Prescription");
            JButton newRef = new JButton("Create Referral");
            bookApp.addActionListener(e -> openBookAppointmentDialog(rowData));
            newPres.addActionListener(e -> openCreatePrescriptionDialog(rowData));
            newRef.addActionListener(e -> openCreateReferralDialog(rowData));
            actions.add(bookApp);
            actions.add(newPres);
            actions.add(newRef);
        }

        rightPanel.add(info, BorderLayout.CENTER);
        rightPanel.add(actions, BorderLayout.SOUTH);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // ---------- Booking & clinician availability ----------
    private void openBookAppointmentDialog(String[] patientRow) {
        // Load clinicians for choice
        List<String[]> clinicians = DataManager.loadCSV(viewFolder + "clinicians.csv");
        String[] clinicianNames = clinicians.size() > 1 ? clinicians.get(0) : new String[0]; // headers
        // build clinician choices as "id - name" from rows
        List<String> clinicianOptions = new ArrayList<>();
        for (int i=1;i<clinicians.size();i++) {
            String[] c = clinicians.get(i);
            String cid = c.length>0?c[0]:"C?";
            String cname = c.length>1?c[1]:"Unknown";
            clinicianOptions.add(cid + " - " + cname);
        }
        JComboBox<String> clinicianBox = new JComboBox<>(clinicianOptions.toArray(new String[0]));
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)); // yyyy-mm-dd
        JTextField timeField = new JTextField("09:00"); // HH:mm

        Object[] fields = {
            "Patient:", patientRow[1] + " (" + patientRow[0] + ")",
            "Clinician:", clinicianBox,
            "Date (yyyy-mm-dd):", dateField,
            "Time (HH:mm):", timeField
        };

        int opt = JOptionPane.showConfirmDialog(this, fields, "Book Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;

        String selectedClinText = (String) clinicianBox.getSelectedItem();
        String clinicianId = selectedClinText.split(" - ")[0];
        String date = dateField.getText();
        String time = timeField.getText();

        // check clinician availability by scanning appointments.csv
        List<String[]> appts = DataManager.loadCSV(viewFolder + "appointments.csv");
        boolean conflict = false;
        for (int i=1;i<appts.size();i++) {
            String[] a = appts.get(i);
            // assuming columns: id, patientId, clinicianId, date, time, status
            if (a.length >= 5 && a[2].equals(clinicianId) && a[3].equals(date) && a[4].equals(time)) {
                conflict = true; break;
            }
        }
        if (conflict) {
            JOptionPane.showMessageDialog(this, "Clinician is not available at that slot. Please choose another time or clinician.");
            return;
        }

        // create appointment id and append
        String apptId = "A" + System.currentTimeMillis();
        String[] newAppt = new String[] { apptId, patientRow[0], clinicianId, date, time, "Booked" };
        appts.add(newAppt);
        DataManager.saveCSV(viewFolder + "appointments.csv", appts);
        JOptionPane.showMessageDialog(this, "Appointment booked: " + apptId);
        // optionally refresh appointment view
        if (currentEntity.equals("appointments")) loadCSVToTable("appointments.csv");
    }

    // ---------- Prescription (simplified) ----------
    private void openCreatePrescriptionDialog(String[] patientRow) {
        // Ask clinician id (or choose)
        List<String[]> clinicians = DataManager.loadCSV(viewFolder + "clinicians.csv");
        List<String> clinicianOptions = new ArrayList<>();
        for (int i=1;i<clinicians.size();i++) clinicianOptions.add(clinicians.get(i)[0] + " - " + clinicians.get(i)[1]);
        JComboBox<String> clinicianBox = new JComboBox<>(clinicianOptions.toArray(new String[0]));
        JTextField meds = new JTextField();
        JTextArea notes = new JTextArea(5,20);
        Object[] fields = {
            "Patient:", patientRow[1] + " (" + patientRow[0] + ")",
            "Clinician:", clinicianBox,
            "Medications (comma-separated):", meds,
            "Notes:", new JScrollPane(notes)
        };
        int opt = JOptionPane.showConfirmDialog(this, fields, "Create Prescription", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;

        String clinicianId = ((String)clinicianBox.getSelectedItem()).split(" - ")[0];
        String medsTxt = meds.getText();
        String notesTxt = notes.getText();
        String prescId = "PR" + System.currentTimeMillis();

        // read prescriptions csv, append
        List<String[]> prescs = DataManager.loadCSV(viewFolder + "prescriptions.csv");
        // header must match expected columns
        String[] newRow = new String[] { prescId, patientRow[0], clinicianId, medsTxt, notesTxt, LocalDateTime.now().toString() };
        prescs.add(newRow);
        DataManager.saveCSV(viewFolder + "prescriptions.csv", prescs);

        // also write text file output as required
        writeTextOutput("outputs/prescription_" + prescId + ".txt",
            "Prescription ID: " + prescId + "\nPatient: " + patientRow[1] + " (" + patientRow[0] + ")\nClinician: " + clinicianId +
            "\nMedications: " + medsTxt + "\nNotes: " + notesTxt);

        JOptionPane.showMessageDialog(this, "Prescription created: " + prescId);
        if (currentEntity.equals("prescriptions")) loadCSVToTable("prescriptions.csv");
    }

    // ---------- Referral using Singleton ----------
    private void openCreateReferralDialog(String[] patientRow) {
        String referralId = ReferralManager.getInstance().generateReferralId();
        String reason = JOptionPane.showInputDialog(this, "Referral reason / notes:");
        if (reason == null) return;

        // create referral row and save
        List<String[]> refs = DataManager.loadCSV(viewFolder + "referrals.csv");
        String[] newRow = new String[] { referralId, patientRow[0], reason, LocalDate.now().toString() };
        refs.add(newRow);
        DataManager.saveCSV(viewFolder + "referrals.csv", refs);

        // also output text file
        writeTextOutput("outputs/referral_" + referralId + ".txt",
            "Referral ID: " + referralId + "\nPatient: " + patientRow[1] + " (" + patientRow[0] + ")\nReason: " + reason);

        JOptionPane.showMessageDialog(this, "Referral created: " + referralId);
        if (currentEntity.equals("referrals")) loadCSVToTable("referrals.csv");
    }

    // helper to write text files
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

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Admin a = new Admin();
            a.setVisible(true);
        });
    }
}
