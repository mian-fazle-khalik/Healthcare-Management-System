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
    private JButton adminBtn, patientBtn, addBtn, editBtn, delBtn, refreshBtn;
    private JPanel mainContainer;
    
  	
//	public Admin() {
//	    setTitle("Healthcare Dashboard - Role Selection");
//	    setSize(1100, 650);
//	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    setLocationRelativeTo(null);
//
//	    mainContainer = new JPanel(new CardLayout());
//	    add(mainContainer);
//
//	    // Role selection screen
//	    JPanel rolePanel = new JPanel(new GridBagLayout());
//	    GridBagConstraints gbc = new GridBagConstraints();
//	    gbc.insets = new Insets(10, 10, 10, 10);
//	    gbc.gridx = 0; gbc.gridy = 0;
//
//	    JLabel title = new JLabel("Select Role to Continue:");
//	    title.setFont(new Font("Arial", Font.BOLD, 18));
//	    rolePanel.add(title, gbc);
//
//	    gbc.gridy++;
//	    adminBtn = new JButton("Admin");
//	    adminBtn.setPreferredSize(new Dimension(180, 40));
//	    rolePanel.add(adminBtn, gbc);
//
//	    gbc.gridy++;
//	    patientBtn = new JButton("Patient");
//	    patientBtn.setPreferredSize(new Dimension(180, 40));
//	    rolePanel.add(patientBtn, gbc);
//
//	    mainContainer.add(rolePanel, "ROLE"); // first screen
//
//	    // Build the dashboard but don’t show yet
//	    JPanel dashboardPanel = buildDashboard();
//	    mainContainer.add(dashboardPanel, "DASHBOARD");
//
//	    // Listeners for switching roles
//	    adminBtn.addActionListener(e -> switchToAdminView());
//	    patientBtn.addActionListener(e -> switchToPatientView());
//	}
//	
//	private JPanel buildDashboard() {
//	    buildNavPanel();
//	    buildCenterPanel();
//	    buildRightPanel();
//
//	    // Create the split pane for bottom layout
//	    JSplitPane centerBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, rightPanel);
//	    centerBottom.setDividerLocation(400);
//
//	    // Combine navigation + main content
//	    JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, centerBottom);
//	    mainSplit.setDividerLocation(180);
//
//	    // Wrap everything in a panel
//	    JPanel dashPanel = new JPanel(new BorderLayout());
//	    dashPanel.add(mainSplit, BorderLayout.CENTER);
//
//	    return dashPanel;
//	}

    
    public Admin() {
        setTitle("Healthcare Dashboard - Role Selection");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // CardLayout container for role selection and dashboard
        mainContainer = new JPanel(new CardLayout());
        add(mainContainer);

        // --- ROLE SELECTION SCREEN ---
        JPanel rolePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel title = new JLabel("Select Role to Continue:");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        rolePanel.add(title, gbc);

        gbc.gridy++;
        adminBtn = new JButton("Admin");
        adminBtn.setPreferredSize(new Dimension(180, 40));
        rolePanel.add(adminBtn, gbc);

        gbc.gridy++;
        patientBtn = new JButton("Patient");
        patientBtn.setPreferredSize(new Dimension(180, 40));
        rolePanel.add(patientBtn, gbc);

        mainContainer.add(rolePanel, "ROLE"); // first screen

        // --- DASHBOARD PANEL (hidden initially) ---
        JPanel dashboardPanel = buildDashboard();  // this uses bottom panel for details/actions
        mainContainer.add(dashboardPanel, "DASHBOARD");

        // --- ACTION LISTENERS ---
        adminBtn.addActionListener(e -> {
            switchToAdminView();  // sets button visibility, nav, etc.
            showCard("DASHBOARD");
        });

        patientBtn.addActionListener(e -> {
            switchToPatientView();  // sets button visibility, nav, etc.
            showCard("DASHBOARD");
        });
    }

    // --- Helper to switch cards ---
    private void showCard(String name) {
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        cl.show(mainContainer, name);
    }

    // --- BUILD DASHBOARD WITH BOTTOM PANEL ---
    private JPanel buildDashboard() {
        buildNavPanel();    // left sidebar
        buildCenterPanel(); // table + toolbar
        buildRightPanel();  // bottom details/actions panel

        JSplitPane centerBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, rightPanel);
        centerBottom.setDividerLocation(400); // top = table, bottom = details

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, centerBottom);
        mainSplit.setDividerLocation(180);

        JPanel dashPanel = new JPanel(new BorderLayout());
        dashPanel.add(mainSplit, BorderLayout.CENTER);
        return dashPanel;
    }


    
	private void switchToAdminView() {
	    setTitle("Healthcare Dashboard - Admin View");
	    CardLayout cl = (CardLayout) mainContainer.getLayout();
	    cl.show(mainContainer, "DASHBOARD");

	    navPanel.setVisible(true);
	    addBtn.setVisible(true);
	    editBtn.setVisible(true);
	    delBtn.setVisible(true);
	    refreshBtn.setVisible(true);

	    showEntity("patients");
	}


	private void switchToPatientView() {
	    setTitle("Healthcare Dashboard - Patient View");
	    CardLayout cl = (CardLayout) mainContainer.getLayout();
	    cl.show(mainContainer, "DASHBOARD");

	    navPanel.setVisible(false); // hide left sidebar
	    addBtn.setVisible(false);
	    editBtn.setVisible(false);
	    delBtn.setVisible(false);
	    refreshBtn.setVisible(true);

	    showEntity("appointments");

	    JOptionPane.showMessageDialog(this,
	        "Patient view active.\nYou can view, book, modify or cancel appointments.",
	        "Patient Mode", JOptionPane.INFORMATION_MESSAGE);
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

 // Utility method to build a panel with record details
    private JPanel createDetailsPanel(Map<String, String> details) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 0, 5)); // (rows=auto, cols=1, hgap=0, vgap=5)
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Map.Entry<String, String> entry : details.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
            panel.add(label);
        }

        return panel;
    }

    
    private void buildCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());

        // toolbar with Add/Edit/Delete/Refresh
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        delBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");

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

    private void showEntity(String entity) {
        currentEntity = entity;
        loadCSVToTable(entity + ".csv");
        updateToolbarButtons();
    }

    // ---------- NEW METHOD: control button states ----------
    private void updateToolbarButtons() {
        if (addBtn == null || editBtn == null || delBtn == null) return;

        // Default all disabled first
        addBtn.setEnabled(false);
        editBtn.setEnabled(false);
        delBtn.setEnabled(false);

        switch (currentEntity) {
            case "patients":
            case "clinicians":
            case "appointments":
            case "staff":
                addBtn.setEnabled(true);
                editBtn.setEnabled(true);
                delBtn.setEnabled(true);
                break;

            case "facilities":
            case "prescriptions":
            case "referrals":
                addBtn.setEnabled(true);
                editBtn.setEnabled(true);
                delBtn.setEnabled(false);
                break;

            default:
                // no entity matched
                break;
        }
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
 // ---------- Patients dialogs ----------
    private void openAddPatientDialog() {
        String newId = generateSequentialId("P", currentData); // e.g., P12
        JTextField idF = new JTextField(newId);
        idF.setEditable(false);

        JTextField firstNameF = new JTextField();
        JTextField lastNameF = new JTextField();
        JTextField dobF = new JTextField(LocalDate.now().toString()); // yyyy-MM-dd
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

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] newRow = new String[] {
                idF.getText(), firstNameF.getText(), lastNameF.getText(), dobF.getText(), nhsF.getText(),
                genderF.getText(), phoneF.getText(), emailF.getText(), addressF.getText(), postcodeF.getText(),
                emergencyNameF.getText(), emergencyPhoneF.getText(), registrationF.getText(), gpSurgeryF.getText()
            };

            currentData.add(newRow);
            DataManager.saveCSV(viewFolder + "patients.csv", currentData);
            refreshCurrent();
            selectRowById(idF.getText());
        }
    }

    private void openEditPatientDialog(int selectedRow) {
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

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String[] updatedRow = new String[] {
                idF.getText(), firstNameF.getText(), lastNameF.getText(), dobF.getText(), nhsF.getText(),
                genderF.getText(), phoneF.getText(), emailF.getText(), addressF.getText(), postcodeF.getText(),
                emergencyNameF.getText(), emergencyPhoneF.getText(), registrationF.getText(), gpSurgeryF.getText()
            };

            currentData.set(modelRow + 1, updatedRow);
            DataManager.saveCSV(viewFolder + "patients.csv", currentData);
            refreshCurrent();
            selectRowById(idF.getText());
        }
    }

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
    
 // ---------- Helper: select a row in the table by ID ----------
    private void selectRowById(String id) {
        if (mainTable == null || tableModel == null) return;

        for (int i = 0; i < mainTable.getRowCount(); i++) {
            Object value = mainTable.getValueAt(i, 0); // assuming ID is in the first column
            if (value != null && value.toString().equals(id)) {
                mainTable.setRowSelectionInterval(i, i);
                mainTable.scrollRectToVisible(mainTable.getCellRect(i, 0, true));
                break;
            }
        }
    }


    // ---------- Booking & clinician availability ----------
    private void openBookAppointmentDialog(String[] patientRow) {
        // Load clinicians for choice
        List<String[]> clinicians = DataManager.loadCSV(viewFolder + "clinicians.csv");
        List<String> clinicianOptions = new ArrayList<>();

        // Build clinician choices as "id - name"
        for (int i = 1; i < clinicians.size(); i++) {
            String[] c = clinicians.get(i);
            String cid = c.length > 0 ? c[0] : "C?";
            String cname = c.length > 1 ? c[1] : "Unknown";
            clinicianOptions.add(cid + " - " + cname);
        }

        if (clinicianOptions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No clinicians found. Please add clinicians first.");
            return;
        }

        JComboBox<String> clinicianBox = new JComboBox<>(clinicianOptions.toArray(new String[0]));
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)); // yyyy-MM-dd
        JTextField timeField = new JTextField("09:00"); // HH:mm

        Object[] fields = {
            "Patient:", patientRow[1] + " (" + patientRow[0] + ")", // name + id
            "Clinician:", clinicianBox,
            "Date (yyyy-mm-dd):", dateField,
            "Time (HH:mm):", timeField
        };

        int opt = JOptionPane.showConfirmDialog(this, fields, "Book Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;

        String selectedClinText = (String) clinicianBox.getSelectedItem();
        String clinicianId = selectedClinText.split(" - ")[0];
        String clinicianName = selectedClinText.split(" - ")[1];
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        // Validate inputs
        if (date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both date and time.");
            return;
        }

        // Check clinician availability in appointments.csv
        List<String[]> appts = DataManager.loadCSV(viewFolder + "appointments.csv");
        boolean conflict = false;

        for (int i = 1; i < appts.size(); i++) {
            String[] a = appts.get(i);
            // Expected: AppointmentID, PatientID, PatientName, ClinicianID, ClinicianName, Date, Time, Status
            if (a.length >= 7 && a[3].equals(clinicianId) && a[5].equals(date) && a[6].equals(time)) {
                conflict = true;
                break;
            }
        }

        if (conflict) {
            JOptionPane.showMessageDialog(this, "Clinician is not available at that slot. Please choose another time or clinician.");
            return;
        }

        // Create new appointment row
        String apptId = "A" + System.currentTimeMillis();
        String patientId = patientRow[0];
        String patientName = patientRow[1];
        String[] newAppt = new String[] {
            apptId, patientId, patientName, clinicianId, clinicianName, date, time, "Booked"
        };

        // Ensure header exists
        if (appts.isEmpty()) {
            appts.add(new String[] {
                "AppointmentID", "PatientID", "PatientName",
                "ClinicianID", "ClinicianName", "Date", "Time", "Status"
            });
        } else {
            // Verify header count (safety check)
            if (appts.get(0).length < 8) {
                appts.clear();
                appts.add(new String[] {
                    "AppointmentID", "PatientID", "PatientName",
                    "ClinicianID", "ClinicianName", "Date", "Time", "Status"
                });
            }
        }

        // Add appointment and save
        appts.add(newAppt);
        DataManager.saveCSV(viewFolder + "appointments.csv", appts);

        JOptionPane.showMessageDialog(this, "Appointment booked successfully: " + apptId);

        // Refresh if currently viewing appointments
        if (currentEntity.equals("appointments")) {
            loadCSVToTable("appointments.csv");
        }
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

 // ---------- Update Right Panel when a row is selected ----------
    private void updateRightPanelSelection() {
        int sel = mainTable.getSelectedRow();
        if (sel < 0 || currentData.size() <= 1) {
            rightPanel.removeAll();
            rightPanel.add(new JLabel("Select a record from the table."), BorderLayout.NORTH);
            rightPanel.revalidate();
            rightPanel.repaint();
            return;
        }

        int modelRow = mainTable.convertRowIndexToModel(sel);
        String[] row = currentData.get(modelRow + 1);
        String[] headers = currentData.get(0);

        // Build details map
        Map<String, String> details = new LinkedHashMap<>();
        for (int i = 0; i < headers.length && i < row.length; i++) {
            details.put(headers[i], row[i]);
        }

        JPanel detailsPanel = createDetailsPanel(details);

        // Build action buttons (depending on currentEntity)
        JPanel actionsPanel = new JPanel(new FlowLayout());
        if (currentEntity.equals("patients")) {
            JButton bookBtn = new JButton("Book Appointment");
            JButton prescBtn = new JButton("Create Prescription");
            JButton refBtn = new JButton("Create Referral");

            bookBtn.addActionListener(e -> openBookAppointmentDialog(row));
            prescBtn.addActionListener(e -> openCreatePrescriptionDialog(row));
            refBtn.addActionListener(e -> openCreateReferralDialog(row));

            actionsPanel.add(bookBtn);
            actionsPanel.add(prescBtn);
            actionsPanel.add(refBtn);
        }

        rightPanel.removeAll();
        rightPanel.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        rightPanel.add(actionsPanel, BorderLayout.SOUTH);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Admin a = new Admin();
            a.setVisible(true);
        });
        
        
    }
}