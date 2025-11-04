package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EntryDialog extends JDialog {

    private Map<String, JTextField> fieldMap = new HashMap<>();
    private boolean confirmed = false;

    public EntryDialog(JFrame parent, String title, String[] headers, String[] existingData, Map<String, String> prefill) {
        super(parent, title, true);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(headers.length, 2, 10, 10));
        for (int i = 0; i < headers.length; i++) {
            form.add(new JLabel(headers[i] + ": "));
            JTextField tf = new JTextField();

            // Pre-fill either from existingData (edit) or prefill map (add)
            if (existingData != null) tf.setText(existingData[i]);
            else if (prefill != null && prefill.containsKey(headers[i])) tf.setText(prefill.get(headers[i]));

            // Make ID field non-editable
            if ("Patient ID".equalsIgnoreCase(headers[i])) tf.setEditable(false);

            fieldMap.put(headers[i], tf);
            form.add(tf);
        }

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        okBtn.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        cancelBtn.addActionListener(e -> setVisible(false));

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String[] getData() {
        String[] data = new String[fieldMap.size()];
        int i = 0;
        for (String key : fieldMap.keySet()) {
            data[i++] = fieldMap.get(key).getText();
        }
        return data;
    }
}
