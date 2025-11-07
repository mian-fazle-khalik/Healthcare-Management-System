package controller;

import model.DataManager;
import model.ReferralManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

	// These are used by some methods (kept for compatibility with AdminUI-style
	// code)
	private JTable table;
	private DefaultTableModel model;
	private JPanel detailsPanel;

	private Consumer<String> entitySelectionListener;
	private Runnable addListener;
	private Consumer<Integer> editListener;
	private Consumer<Integer> deleteListener;
	private Consumer<Integer> rowSelectionListener;

	// Single DialogController instance
	private DialogController dialogController;

	private JButton processRescheduleBtn; // class-level variable

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
		gbc.gridx = 0;
		gbc.gridy = 0;

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

		// Build dashboard and add as card
		JPanel dashboardPanel = buildDashboard();
		mainContainer.add(dashboardPanel, "DASHBOARD");

		// DialogController will be initialized in buildCenterPanel (after mainTable
		// exists)
		// But keep a safe init here - buildCenterPanel creates it already.

		// --- ACTION LISTENERS ---
		adminBtn.addActionListener(e -> switchToAdminView());
		patientBtn.addActionListener(e -> switchToPatientView());
	}

	// Build dashboard by composing nav, center, right
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
		// Ensure toolbar buttons visible
		if (navPanel != null)
			navPanel.setVisible(true);
		if (addBtn != null)
			addBtn.setVisible(true);
		if (editBtn != null)
			editBtn.setVisible(true);
		if (delBtn != null)
			delBtn.setVisible(true);
		if (refreshBtn != null)
			refreshBtn.setVisible(true);

		// Add processRescheduleBtn to toolbar if present and not already added
		try {
			if (centerPanel != null && processRescheduleBtn != null) {
				Component comp = centerPanel.getComponent(0); // toolbar assumed at index 0
				if (comp instanceof JPanel) {
					JPanel toolbar = (JPanel) comp;
					if (processRescheduleBtn.getParent() != toolbar) {
						toolbar.add(processRescheduleBtn);
					}
					processRescheduleBtn.setVisible(true);
				}
			}
		} catch (Exception ignored) {
		}

		// show patients by default
		showEntity("patients");
		showCard("DASHBOARD");

		ReferralManager.getInstance().logAction("Switched to Admin view");
		System.out.println("[UI] Switched to Admin view");
	}

	private void switchToPatientView() {
		setTitle("Healthcare Dashboard - Patient View");

		// Hide admin buttons
		if (navPanel != null)
			navPanel.setVisible(false);
		if (addBtn != null)
			addBtn.setVisible(false);
		if (editBtn != null)
			editBtn.setVisible(false);
		if (delBtn != null)
			delBtn.setVisible(false);
		if (refreshBtn != null)
			refreshBtn.setVisible(true);

		// Load patients table
		showEntity("patients");
		showCard("DASHBOARD");

		// Add "Search for Appointments" button below table (temporary)
		JButton searchAppointmentsBtn = new JButton("Search for Appointments");
		searchAppointmentsBtn.addActionListener(e -> openSearchAppointmentsDialog());

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(searchAppointmentsBtn);

		try {
			centerPanel.add(bottomPanel, BorderLayout.SOUTH);
		} catch (Exception ignored) {
		}

		JOptionPane.showMessageDialog(this, "Patient view active.\nYou can view, book, modify or cancel appointments.",
				"Patient Mode", JOptionPane.INFORMATION_MESSAGE);

		ReferralManager.getInstance().logAction("Switched to Patient view");
		System.out.println("[UI] Switched to Patient view");
	}

	private void buildNavPanel() {
		navPanel = new JPanel();
		navPanel.setLayout(new GridLayout(0, 1, 5, 5));
		navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		String[] entities = { "patients", "clinicians", "appointments", "prescriptions", "referrals" };
		for (String ent : entities) {
			JButton b = new JButton(ent.substring(0, 1).toUpperCase() + ent.substring(1));

			// Highlight the button when clicked and show the entity
			b.addActionListener(e -> {
				// Logging
				ReferralManager.getInstance().logAction("Button clicked: " + ent);
				System.out.println("[UI] " + ent + " button clicked");

				showEntity(ent); // switch table to this entity
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

		// --- ADD button ---
		addBtn.addActionListener(e -> {
			ReferralManager.getInstance().logAction("[UI] Add button clicked for entity: " + currentEntity);
			if (addListener != null)
				addListener.run();
		});

//         --- EDIT button ---
		editBtn.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			ReferralManager.getInstance().logAction("[UI] Edit button clicked. Selected row: " + selectedRow);
			if (editListener != null)
				editListener.accept(selectedRow);
		});

		// --- DELETE button ---
		delBtn.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			ReferralManager.getInstance().logAction("[UI] Delete button clicked. Selected row: " + selectedRow);
			if (deleteListener != null)
				deleteListener.accept(selectedRow);
		});

		// --- REFRESH button ---
		refreshBtn.addActionListener(e -> {
			ReferralManager.getInstance().logAction("[UI] Refresh button clicked for entity: " + currentEntity);
//            loadEntityData(currentEntity); // if you already have this function
		});

		addBtn.addActionListener(e -> handleAdd());
		editBtn.addActionListener(e -> handleEdit());
		delBtn.addActionListener(e -> handleDelete());
		refreshBtn.addActionListener(e -> refreshCurrent());

		toolbar.add(addBtn);
		toolbar.add(editBtn);
		toolbar.add(delBtn);
		toolbar.add(refreshBtn);

		// Process Reschedule button (added dynamically in admin view)
		processRescheduleBtn = new JButton("Process Reschedules");
		processRescheduleBtn.addActionListener(e -> autoRescheduleAppointments());

		centerPanel.add(toolbar, BorderLayout.NORTH);

		tableModel = new DefaultTableModel();
		mainTable = new JTable(tableModel);
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting())
				updateRightPanelSelection();
		});

		tableScroll = new JScrollPane(mainTable);
		centerPanel.add(tableScroll, BorderLayout.CENTER);

		// Initialize DialogController with correct references
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
		if (addBtn == null || editBtn == null || delBtn == null)
			return;
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
		case "prescriptions":
		case "referrals":
			addBtn.setEnabled(true);
			editBtn.setEnabled(true);
			delBtn.setEnabled(true);
			break;
		}
	}

	private void loadCSVToTable(String filename) {
		currentData = DataManager.loadCSV(viewFolder + filename);
		SwingUtilities.invokeLater(() -> {
			tableModel.setRowCount(0);
			tableModel.setColumnCount(0);
			if (currentData == null || currentData.size() == 0) {
				tableModel.addColumn("Empty");
				return;
			}
			String[] header = currentData.get(0);
			for (String h : header)
				tableModel.addColumn(h);
			for (int i = 1; i < currentData.size(); i++)
				tableModel.addRow(currentData.get(i));
		});

		// Update DialogController reference so dialogs have fresh data and table
		// reference
		dialogController = new DialogController(viewFolder, mainTable, currentData);
	}

	private void refreshCurrent() {
		showEntity(currentEntity);
	}

	private void handleAdd() {
		ReferralManager.getInstance().logAction("[UI] Add button clicked for entity: " + currentEntity);
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
		default:
			JOptionPane.showMessageDialog(this, "Add not implemented for " + currentEntity);
		}
		ReferralManager.getInstance().logAction("[DATA] Record added successfully in " + currentEntity);
		refreshCurrent();
	}

	private void handleEdit() {
		int sel = mainTable.getSelectedRow();
		if (sel < 0) {
			ReferralManager.getInstance().logAction("[WARN] Edit button clicked but no row selected");
			JOptionPane.showMessageDialog(this, "Select a row first.");
			return;
		}

		ReferralManager.getInstance().logAction("[UI] Edit button clicked for " + currentEntity + " - Row: " + sel);

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
		default:
			JOptionPane.showMessageDialog(this, "Edit not implemented for " + currentEntity);
		}
		ReferralManager.getInstance().logAction("[DATA] Row " + sel + " edited successfully in " + currentEntity);
		refreshCurrent();
	}

	private void handleDelete() {
		int sel = mainTable.getSelectedRow();
		if (sel < 0) {
			ReferralManager.getInstance().logAction("[WARN] Delete button clicked but no row selected");
			JOptionPane.showMessageDialog(this, "Select a row first.");
			return;
		}

		ReferralManager.getInstance().logAction("[UI] Delete button clicked for " + currentEntity + " - Row: " + sel);

		int modelRow = mainTable.convertRowIndexToModel(sel);
		int confirm = JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			currentData.remove(modelRow + 1);
			DataManager.saveCSV(viewFolder + currentEntity + ".csv", currentData);
			ReferralManager.getInstance()
					.logAction("[DATA] Row " + sel + " deleted successfully from " + currentEntity);
			refreshCurrent();
		} else {
			ReferralManager.getInstance().logAction("[CANCEL] Delete operation canceled for row " + sel);
		}
	}

	private String[] findNextAvailableSlot(String clinicianId, String facilityId, int duration,
			List<String[]> appointmentsData) {
		LocalDate date = LocalDate.now();
		LocalTime startTime = LocalTime.of(9, 0); // clinic opens at 9 AM
		LocalTime endTime = LocalTime.of(17, 0); // closes at 5 PM
		int increment = 15; // minutes between attempts

		while (true) {
			LocalTime time = startTime;
			while (!time.plusMinutes(duration).isAfter(endTime)) {
				boolean conflict = false;
				for (int i = 1; i < appointmentsData.size(); i++) {
					String[] appt = appointmentsData.get(i);
					if (!appt[2].equals(clinicianId))
						continue; // different clinician
					if (!appt[3].equals(facilityId))
						continue; // different facility

					LocalDate apptDate = LocalDate.parse(appt[4]);
					LocalTime apptStart = LocalTime.parse(appt[5]);
					int apptDuration = Integer.parseInt(appt[6]);
					LocalTime apptEnd = apptStart.plusMinutes(apptDuration);

					if (apptDate.equals(date)
							&& !(time.plusMinutes(duration).isBefore(apptStart) || time.isAfter(apptEnd))) {
						conflict = true;
						break;
					}
				}
				if (!conflict)
					return new String[] { date.toString(), time.toString() };
				time = time.plusMinutes(increment);
			}
			date = date.plusDays(1); // try next day
		}
	}

	private void autoRescheduleAppointments() {
		List<String[]> appointmentsData = DataManager.loadCSV(viewFolder + "appointments.csv");
		boolean updated = false;

		for (int i = 1; i < appointmentsData.size(); i++) {
			String[] appt = appointmentsData.get(i);

			if ("Reschedule".equalsIgnoreCase(appt[8])) { // pending reschedule
				String clinicianId = appt[2];
				String facilityId = appt[3];
				String date = appt[4];

				// Try up to 30 days ahead
				for (int offset = 0; offset < 30; offset++) {
					LocalDate newDate = LocalDate.parse(date).plusDays(offset);
					if (isDateAvailable(clinicianId, facilityId, newDate, appointmentsData)) {
						appt[4] = newDate.toString();
						appt[8] = "Scheduled"; // ✅ Update status to scheduled
						updated = true;
						break;
					}
				}
			}
		}

		if (updated) {
			DataManager.saveCSV(viewFolder + "appointments.csv", appointmentsData);
			JOptionPane.showMessageDialog(this, "Reschedule appointments have been scheduled.");
			refreshCurrent(); // reload table
		} else {
			JOptionPane.showMessageDialog(this, "No available slots found for reschedules.");
		}
	}

	private boolean isDateAvailable(String clinicianId, String facilityId, LocalDate date,
			List<String[]> appointmentsData) {
		for (int i = 1; i < appointmentsData.size(); i++) {
			String[] appt = appointmentsData.get(i);
			if (!appt[2].equals(clinicianId) || !appt[3].equals(facilityId))
				continue;
			if (LocalDate.parse(appt[4]).equals(date) && !"Cancelled".equalsIgnoreCase(appt[8]))
				return false;
		}
		return true;
	}

	private void openSearchAppointmentsDialog() {
		// Load patients
		List<String[]> patientsData = DataManager.loadCSV(viewFolder + "patients.csv");
		if (patientsData == null || patientsData.size() <= 1) {
			JOptionPane.showMessageDialog(this, "No patients available.");
			return;
		}
		String[] patientOptions = patientsData.stream().skip(1).map(p -> p[0] + " - " + p[1] + " " + p[2])
				.toArray(String[]::new);

		JComboBox<String> patientCombo = new JComboBox<>(patientOptions);
		int option = JOptionPane.showConfirmDialog(this, patientCombo, "Select Patient", JOptionPane.OK_CANCEL_OPTION);
		if (option != JOptionPane.OK_OPTION)
			return;

		String selected = (String) patientCombo.getSelectedItem();
		if (selected == null)
			return;
		String patientId = selected.split(" - ")[0];

		// Load appointments
		List<String[]> appointmentsData = DataManager.loadCSV(viewFolder + "appointments.csv");
		String[] header = appointmentsData.get(0);

		// Filter appointments for patient
		List<String[]> patientAppointments = new ArrayList<>();
		patientAppointments.add(header);
		for (int i = 1; i < appointmentsData.size(); i++) {
			String[] appt = appointmentsData.get(i);
			if (appt[1].equals(patientId))
				patientAppointments.add(appt);
		}

		// Table
		DefaultTableModel apptModel = new DefaultTableModel();
		JTable apptTable = new JTable(apptModel) {
			@Override
			public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
				Component comp = super.prepareRenderer(renderer, row, column);
				if (row >= 0 && getModel().getRowCount() > 0) {
					Object statusObj = getModel().getValueAt(row, 8);
					String status = statusObj == null ? "" : statusObj.toString();
					if ("Cancelled".equalsIgnoreCase(status))
						comp.setBackground(Color.PINK);
					else if ("Reschedule".equalsIgnoreCase(status))
						comp.setBackground(Color.YELLOW);
					else
						comp.setBackground(Color.WHITE);
				}
				return comp;
			}
		};

		// Populate table
		for (String col : patientAppointments.get(0))
			apptModel.addColumn(col);
		for (int i = 1; i < patientAppointments.size(); i++)
			apptModel.addRow(patientAppointments.get(i));

		JScrollPane scroll = new JScrollPane(apptTable);
		scroll.setPreferredSize(new Dimension(900, 300));

		// Buttons
		JButton cancelBtn = new JButton("Cancel");
		JButton rescheduleBtn = new JButton("Reschedule");

		cancelBtn.addActionListener(e -> {
			int sel = apptTable.getSelectedRow();
			if (sel < 0) {
				JOptionPane.showMessageDialog(this, "Select an appointment to cancel.");
				return;
			}

			String appointmentId = (String) apptModel.getValueAt(sel, 0);
			apptModel.removeRow(sel);
			appointmentsData.removeIf(appt -> appt[0].equals(appointmentId));
			DataManager.saveCSV(viewFolder + "appointments.csv", appointmentsData);

			JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.");
		});

		rescheduleBtn.addActionListener(e -> {
			int sel = apptTable.getSelectedRow();
			if (sel < 0) {
				JOptionPane.showMessageDialog(this, "Select an appointment to reschedule.");
				return;
			}

			apptModel.setValueAt("Reschedule", sel, 8); // status index = 8
			String appointmentId = (String) apptModel.getValueAt(sel, 0);

			for (int i = 1; i < appointmentsData.size(); i++) {
				if (appointmentsData.get(i)[0].equals(appointmentId)) {
					appointmentsData.get(i)[8] = "Reschedule";
					break;
				}
			}
			DataManager.saveCSV(viewFolder + "appointments.csv", appointmentsData);

			JOptionPane.showMessageDialog(this, "Appointment marked for reschedule.");
		});

		JPanel actionPanel = new JPanel();
		actionPanel.add(cancelBtn);
		actionPanel.add(rescheduleBtn);

		JPanel dialogPanel = new JPanel(new BorderLayout());
		dialogPanel.add(scroll, BorderLayout.CENTER);
		dialogPanel.add(actionPanel, BorderLayout.SOUTH);

		JOptionPane.showMessageDialog(this, dialogPanel, "Appointments for " + selected,
				JOptionPane.INFORMATION_MESSAGE);
	}

	// Admin approves or denies reschedules
	private void openApproveRescheduleDialog() {
		List<String[]> appointmentsData = DataManager.loadCSV(viewFolder + "appointments.csv");
		List<String[]> requested = new ArrayList<>();
		requested.add(appointmentsData.get(0)); // header

		for (int i = 1; i < appointmentsData.size(); i++) {
			if ("Reschedule".equalsIgnoreCase(appointmentsData.get(i)[8]))
				requested.add(appointmentsData.get(i));
		}

		if (requested.size() <= 1) {
			JOptionPane.showMessageDialog(this, "No reschedule requests pending.");
			return;
		}

		DefaultTableModel reqModel = new DefaultTableModel();
		JTable reqTable = new JTable(reqModel);
		for (String col : requested.get(0))
			reqModel.addColumn(col);
		for (int i = 1; i < requested.size(); i++)
			reqModel.addRow(requested.get(i));

		JScrollPane scroll = new JScrollPane(reqTable);
		scroll.setPreferredSize(new Dimension(900, 300));

		JButton approveBtn = new JButton("Approve");
		JButton denyBtn = new JButton("Deny");

		approveBtn.addActionListener(e -> {
			int sel = reqTable.getSelectedRow();
			if (sel < 0) {
				JOptionPane.showMessageDialog(this, "Select a request.");
				return;
			}

			String appointmentId = (String) reqModel.getValueAt(sel, 0);
			reqModel.setValueAt("Rescheduled", sel, 8);

			for (int i = 1; i < appointmentsData.size(); i++) {
				if (appointmentsData.get(i)[0].equals(appointmentId)) {
					appointmentsData.get(i)[8] = "Rescheduled";
					break;
				}
			}
			DataManager.saveCSV(viewFolder + "appointments.csv", appointmentsData);
			JOptionPane.showMessageDialog(this, "Reschedule approved.");
		});

		denyBtn.addActionListener(e -> {
			int sel = reqTable.getSelectedRow();
			if (sel < 0)
				return;

			String appointmentId = (String) reqModel.getValueAt(sel, 0);
			reqModel.setValueAt("Denied", sel, 8);

			for (int i = 1; i < appointmentsData.size(); i++) {
				if (appointmentsData.get(i)[0].equals(appointmentId)) {
					appointmentsData.get(i)[8] = "Denied";
					break;
				}
			}
			DataManager.saveCSV(viewFolder + "appointments.csv", appointmentsData);
			JOptionPane.showMessageDialog(this, "Reschedule request denied.");
		});

		JPanel actionPanel = new JPanel();
		actionPanel.add(approveBtn);
		actionPanel.add(denyBtn);

		JPanel dialogPanel = new JPanel(new BorderLayout());
		dialogPanel.add(scroll, BorderLayout.CENTER);
		dialogPanel.add(actionPanel, BorderLayout.SOUTH);

		JOptionPane.showMessageDialog(this, dialogPanel, "Pending Reschedule Requests",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void updateRightPanelSelection() {
		if (mainTable.getSelectedRow() < 0)
			return;
		int row = mainTable.convertRowIndexToModel(mainTable.getSelectedRow());
		String[] dataRow = currentData.get(row + 1);
		JPanel details = new JPanel(new GridLayout(0, 1));
		for (int i = 0; i < dataRow.length; i++) {
			details.add(new JLabel(currentData.get(0)[i] + ": " + dataRow[i]));
		}
		rightPanel.removeAll();
		rightPanel.add(details, BorderLayout.CENTER);
		rightPanel.revalidate();
		rightPanel.repaint();
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

	private String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private void showCard(String name) {
		CardLayout cl = (CardLayout) mainContainer.getLayout();
		cl.show(mainContainer, name);
	}
	

	public static boolean copyFileContents(String srcPath, String destPath) {
	    File src = new File(srcPath);
	    if (!src.exists()) {
	        System.err.println("[copyFileContents] Source file does not exist: " + srcPath);
	        return false;
	    }
	    // Ensure destination directory exists
	    File dest = new File(destPath);
	    File parent = dest.getParentFile();
	    if (parent != null && !parent.exists()) {
	        boolean created = parent.mkdirs();
	        System.out.println("[copyFileContents] Created destination directories: " + created + " -> " + parent.getAbsolutePath());
	    }

	    try (BufferedReader br = new BufferedReader(new FileReader(src));
	         BufferedWriter bw = new BufferedWriter(new FileWriter(dest, false))) { // false => overwrite
	        String line;
	        int count = 0;
	        while ((line = br.readLine()) != null) {
	            bw.write(line);
	            bw.newLine();
	            count++;
	        }
	        bw.flush();
	        System.out.println("[copyFileContents] Copied " + count + " lines from " + srcPath + " to " + destPath);
	        return true;
	    } catch (IOException e) {
	        System.err.println("[copyFileContents] IO error copying from " + srcPath + " to " + destPath);
	        e.printStackTrace();
	        return false;
	    }
	}


	// === MAIN ===
//	public static void main(String[] args) {
//
//		SwingUtilities.invokeLater(() -> {
//			Admin adminApp = new Admin(); 
//			adminApp.setVisible(true);
//		});
//	}
	
	public static void main(String[] args) {
	    System.out.println("=== Starting Healthcare Dashboard Application ===");

	    String viewFolder = "src/view/"; // adjust if your CSVs are elsewhere
	    String prescriptionsCSV = viewFolder + "prescriptions.csv";
	    String referralsCSV = viewFolder + "referrals.csv";

	    // choose the exact outputs you want
	    String prescriptionsTxt = viewFolder + "prescriptions_output.txt";
	    String referralsTxt = viewFolder + "referrals_output.txt";

	    boolean pOk = copyFileContents(prescriptionsCSV, prescriptionsTxt);
	    boolean rOk = copyFileContents(referralsCSV, referralsTxt);

	    if (!pOk) System.err.println("[MAIN] Failed to produce " + prescriptionsTxt);
	    if (!rOk) System.err.println("[MAIN] Failed to produce " + referralsTxt);
	    if (pOk && rOk) System.out.println("[MAIN] Successfully exported CSVs to text files.");

	    // Launch GUI
	    javax.swing.SwingUtilities.invokeLater(() -> {
	        Admin adminApp = new Admin();
	        adminApp.setVisible(true);
	    });
	}



	
}
