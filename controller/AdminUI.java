package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdminUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JPanel detailsPanel;

    private Consumer<String> entitySelectionListener;
    private Runnable addListener;
    private Consumer<Integer> editListener;
    private Consumer<Integer> deleteListener;
    private Consumer<Integer> rowSelectionListener;

    public AdminUI() {
        setTitle("Admin Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildNavPanel(), buildMainPanel());
        splitPane.setDividerLocation(180);

        add(splitPane);
    }

    private JPanel buildNavPanel() {
        JPanel nav = new JPanel(new GridLayout(0, 1, 5, 5));
        nav.setBorder(BorderFactory.createTitledBorder("Entities"));

        String[] entities = {"patients", "clinicians", "appointments", "prescriptions", "referrals"};
        for (String e : entities) {
            JButton b = new JButton(capitalize(e));
            b.addActionListener(a -> {
                if (entitySelectionListener != null)
                    entitySelectionListener.accept(e);
            });
            nav.add(b);
        }

        return nav;
    }

    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new BorderLayout());

        // toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");

        addBtn.addActionListener(e -> {
            if (addListener != null) addListener.run();
        });
        editBtn.addActionListener(e -> {
            if (editListener != null)
                editListener.accept(table.getSelectedRow());
        });
        delBtn.addActionListener(e -> {
            if (deleteListener != null)
                deleteListener.accept(table.getSelectedRow());
        });
        


        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(delBtn);
        main.add(toolbar, BorderLayout.NORTH);

        // table
        model = new DefaultTableModel();
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && rowSelectionListener != null)
                rowSelectionListener.accept(table.getSelectedRow());
        });
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        // details panel
        detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
        main.add(detailsPanel, BorderLayout.SOUTH);

        return main;
    }

    
    public void updateTable(List<String[]> data) {
        model.setRowCount(0);
        model.setColumnCount(0);

        if (data == null || data.isEmpty()) return;
        for (String col : data.get(0)) model.addColumn(col);
        for (int i = 1; i < data.size(); i++) model.addRow(data.get(i));
    }

    public void showDetailsPanel(Map<String, String> details, String entity, String[] row,
                                 Consumer<String[]> bookAppt, Consumer<String[]> prescribe, Consumer<String[]> refer) {
        detailsPanel.removeAll();

        JPanel info = new JPanel(new GridLayout(0, 1));
        for (Map.Entry<String, String> entry : details.entrySet()) {
            info.add(new JLabel(entry.getKey() + ": " + entry.getValue()));
        }

        JPanel actions = new JPanel(new FlowLayout());
        if ("patients".equals(entity)) {
            JButton bookBtn = new JButton("Book Appointment");
            JButton prescBtn = new JButton("Create Prescription");
            JButton refBtn = new JButton("Create Referral");

            bookBtn.addActionListener(e -> bookAppt.accept(row));
            prescBtn.addActionListener(e -> prescribe.accept(row));
            refBtn.addActionListener(e -> refer.accept(row));
            

            actions.add(bookBtn);
            actions.add(prescBtn);
            actions.add(refBtn);
        }

        detailsPanel.add(new JScrollPane(info), BorderLayout.CENTER);
        detailsPanel.add(actions, BorderLayout.SOUTH);

        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    public void clearDetailsPanel() {
        detailsPanel.removeAll();
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Listener setters
    public void setEntitySelectionListener(Consumer<String> l) { this.entitySelectionListener = l; }
    public void setAddListener(Runnable l) { this.addListener = l; }
    public void setEditListener(Consumer<Integer> l) { this.editListener = l; }
    public void setDeleteListener(Consumer<Integer> l) { this.deleteListener = l; }
    public void setRowSelectionListener(Consumer<Integer> l) { this.rowSelectionListener = l; }
}
