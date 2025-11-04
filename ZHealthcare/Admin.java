package controller;

import model.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Admin extends JFrame {
    private JPanel navPanel, rightPanel, centerPanel;
    private JTable mainTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private String currentEntity = "patients";
    private final String viewFolder = "src/view/";
    private List<String[]> currentData = new ArrayList<>();
    private JButton adminBtn, patientBtn, addBtn, editBtn, delBtn, refreshBtn;
    private JPanel mainContainer;
    private JButton selectedButton = null;

    // Single DialogController instance
    private DialogController dialogController;

    public Admin() {
        setTitle("Healthcare Dashboard - Role Selection");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        JPanel dashboardPanel = buildDashboard();
        mainContainer.add(dashboardPanel, "DASHBOARD");

        // Initialize DialogController
        dialogController = new DialogController(viewFolder, mainTable, currentData);

        // --- ACTION LISTENERS ---
        adminBtn.addActionListener(e -> switchToAdminView());
        patientBtn.addActionListener(e -> switchToPatientView());
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        cl.show(mainContainer, name);
    }
    
    private void selectSidebarButton(JButton btn) {
        // Reset previous button
        if (selectedButton != null) {
            selectedButton.setBackground(UIManager.getColor("Button.background"));
            selectedButton.setForeground(Color.BLACK); // optional
        }

        // Set new button as selected
        btn.setBackground(Color.BLACK); // or any highlight color
        btn.setForeground(Color.WHITE);
        selectedButton = btn;
    }


    private JPanel buildDashboard() {
        buildNavPanel();
        buildCenterPanel();
        buildRightPanel();

        JSplitPane centerBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, rightPanel);
        centerBottom.setDividerLocation(400);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, centerBottom);
        mainSplit.setDividerLocation(180);

        JPanel dashPanel = new JPanel(new BorderLayout());
        dashPanel.add(mainSplit, BorderLayout.CENTER);
        return dashPanel;
    }

    private void switchToAdminView() {
        setTitle("Healthcare Dashboard - Admin View");
        navPanel.setVisible(true);
        addBtn.setVisible(true);
        editBtn.setVisible(true);
        delBtn.setVisible(true);
        refreshBtn.setVisible(true);
        showEntity("patients");
        showCard("DASHBOARD");
    }

    private void switchToPatientView() {
        setTitle("Healthcare Dashboard - Patient View");
        navPanel.setVisible(false);
        addBtn.setVisible(false);
        editBtn.setVisible(false);
        delBtn.setVisible(false);
        refreshBtn.setVisible(true);
        showEntity("appointments");
        showCard("DASHBOARD");
        JOptionPane.showMessageDialog(this,
                "Patient view active.\nYou can view, book, modify or cancel appointments.",
                "Patient Mode", JOptionPane.INFORMATION_MESSAGE);
    }

//    private void buildNavPanel() {
//        navPanel = new JPanel();
//        navPanel.setLayout(new GridLayout(0, 1, 5, 5));
//        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        String[] entities = {"patients", "clinicians", "appointments", "prescriptions", "referrals"};
//        for (String ent : entities) {
//            JButton b = new JButton(ent.substring(0,1).toUpperCase() + ent.substring(1));
//            b.addActionListener(e -> showEntity(ent));
//            navPanel.add(b);
//        }
//    }
    
    private void buildNavPanel() {
        navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(0, 1, 5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] entities = {"patients", "clinicians", "appointments", "prescriptions", "referrals"};
        for (String ent : entities) {
            JButton b = new JButton(ent.substring(0, 1).toUpperCase() + ent.substring(1));

            // Highlight the button when clicked and show the entity
            b.addActionListener(e -> {
                showEntity(ent);        // switch table to this entity
                selectSidebarButton(b); // highlight the selected button
            });

            navPanel.add(b);

            // Optionally, select the first button by default
            if (selectedButton == null && ent.equals("patients")) {
                selectSidebarButton(b);
            }
        }
    }


    private void buildCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());

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

        tableModel = new DefaultTableModel();
        mainTable = new JTable(tableModel);
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateRightPanelSelection();
        });

        tableScroll = new JScrollPane(mainTable);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        // Update DialogController's table reference
        dialogController = new DialogController(viewFolder, mainTable, currentData);
    }

    private void buildRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Details / Actions"));
        JPanel defaultContent = new JPanel(new BorderLayout());
        defaultContent.add(new JLabel("Select a record from the table."), BorderLayout.NORTH);
        rightPanel.add(defaultContent, BorderLayout.CENTER);
    }

    private void showEntity(String entity) {
        currentEntity = entity;
        loadCSVToTable(entity + ".csv");
        updateToolbarButtons();
    }

    private void updateToolbarButtons() {
        if (addBtn == null || editBtn == null || delBtn == null) return;
        addBtn.setEnabled(false); editBtn.setEnabled(false); delBtn.setEnabled(false);

        switch (currentEntity) {
            case "patients":
            case "clinicians":
            case "appointments":
            case "staff":
                addBtn.setEnabled(true); editBtn.setEnabled(true); delBtn.setEnabled(true); break;
            case "prescriptions":
            case "referrals":
                addBtn.setEnabled(true); editBtn.setEnabled(true); delBtn.setEnabled(true); break;
        }
    }

    private void loadCSVToTable(String filename) {
        currentData = DataManager.loadCSV(viewFolder + filename);
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); tableModel.setColumnCount(0);
            if (currentData.size() == 0) { tableModel.addColumn("Empty"); return; }
            String[] header = currentData.get(0);
            for (String h : header) tableModel.addColumn(h);
            for (int i = 1; i < currentData.size(); i++) tableModel.addRow(currentData.get(i));
        });
        dialogController = new DialogController(viewFolder, mainTable, currentData);
    }

    private void refreshCurrent() { showEntity(currentEntity); }

    private void handleAdd() {
        switch (currentEntity) {
            case "patients":
                dialogController.openAddPatientDialog();
                break;
            case "clinicians":
                dialogController.openAddClinicianDialog();
                break;
            case "appointments":
                dialogController.openAddAppointmentDialog();
                break;
            case "prescriptions":
                dialogController.openAddPrescriptionDialog();
                break;
            case "referrals":
                dialogController.openAddReferralDialog();
                break;
            case "staff":
                /* implement if needed */
                break;
            default:
                JOptionPane.showMessageDialog(this, "Add not implemented for " + currentEntity);
        }
        refreshCurrent();
    }

    private void handleEdit() {
        int sel = mainTable.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Select a row first.");
            return;
        }

        switch (currentEntity) {
            case "patients":
                dialogController.openEditPatientDialog(sel);
                break;
            case "clinicians":
                dialogController.openEditClinicianDialog(sel);
                break;
            case "appointments":
                dialogController.openEditAppointmentDialog(sel);
                break;
            case "prescriptions":
                dialogController.openEditPrescriptionDialog(sel);
                break;
            case "referrals":
                dialogController.openEditReferralDialog(sel);
                break;
            case "staff":
                /* implement if needed */
                break;
            default:
                JOptionPane.showMessageDialog(this, "Edit not implemented for " + currentEntity);
        }
        refreshCurrent();
    }



    private void handleDelete() {
        int sel = mainTable.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
        int modelRow = mainTable.convertRowIndexToModel(sel);
        if (JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            currentData.remove(modelRow + 1);
            DataManager.saveCSV(viewFolder + currentEntity + ".csv", currentData);
            refreshCurrent();
        }
    }

    private void updateRightPanelSelection() {
        if (mainTable.getSelectedRow() < 0) return;
        int row = mainTable.convertRowIndexToModel(mainTable.getSelectedRow());
        String[] dataRow = currentData.get(row + 1);
        JPanel details = new JPanel(new GridLayout(0,1));
        for (int i = 0; i < dataRow.length; i++) {
            details.add(new JLabel(currentData.get(0)[i] + ": " + dataRow[i]));
        }
        rightPanel.removeAll();
        rightPanel.add(details, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

public static void main(String[] args) {
    // Ensure GUI runs on the Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
        Admin adminApp = new Admin();
        adminApp.setVisible(true);
    });
}
}

