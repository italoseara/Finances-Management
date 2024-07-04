package dev.components;

import dev.manager.DatabaseManager;
import dev.style.RoundButton;
import dev.style.RoundTextField;
import dev.util.Utilities;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

public class GoalsModal extends JDialog {
    private final RoundTextField name;
    private final RoundTextField target;
    private final RoundTextField current;

    private final Goals goals;

    public GoalsModal(Goals goals){
        this.goals = goals;

        setTitle("New goal");
        boolean isWindows = Utilities.isWindows();
        setSize(400 + (isWindows ? 16 : 0), 375 + (isWindows ? 39 : 0)); // Windows fix
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(null);

        // Modal styling
        setResizable(false);
        setBackground(Color.WHITE);
        getContentPane().setBackground(Color.WHITE);

        Font font = new Font("Inter", Font.PLAIN, 14);

        // Name label (right above the name field)
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 360, 30);
        nameLabel.setFont(font);
        add(nameLabel);

        // Name field
        name = new RoundTextField(10, 10, 10);
        name.setBounds(20, 50, 360, 37);
        name.setFont(font);
        name.setBackground(Color.WHITE);
        name.setForeground(new Color(0x111827));
        name.setBorderColor(new Color(0xe5e5e8));
        name.setPlaceholder("Name");
        name.setPlaceholderColor(new Color(0x6b7280));
        add(name);

        // Target label (right above the target field)
        JLabel targetLabel = new JLabel("Target:");
        targetLabel.setBounds(20, 100, 360, 30);
        targetLabel.setFont(font);
        add(targetLabel);

        // Target field
        target = new RoundTextField(10, 10, 10);
        target.setBounds(20, 130, 360, 37);
        target.setFont(font);
        target.setBackground(Color.WHITE);
        target.setForeground(new Color(0x111827));
        target.setBorderColor(new Color(0xe5e5e8));
        target.setPlaceholder("Target");
        target.setPlaceholderColor(new Color(0x6b7280));
        add(target);

        // Current label (right above the current field)
        JLabel currentLabel = new JLabel("Current:");
        currentLabel.setBounds(20, 180, 360, 30);
        currentLabel.setFont(font);
        add(currentLabel);

        // Current field
        current = new RoundTextField(10, 10, 10);
        current.setBounds(20, 210, 360, 37);
        current.setFont(font);
        current.setBackground(Color.WHITE);
        current.setForeground(new Color(0x111827));
        current.setBorderColor(new Color(0xe5e5e8));
        current.setPlaceholder("Current");
        current.setPlaceholderColor(new Color(0x6b7280));
        add(current);

        // Save button
        RoundButton saveButton = new RoundButton("Save Goal", 10);
        saveButton.setFont(font);
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(new Color(0x111827));
        saveButton.setHoverColor(new Color(0xf8f4f4));
        saveButton.setBorderColor(new Color(0xe5e5e8));
        saveButton.setBounds(20, 270, 360, 37);
        saveButton.addActionListener(e -> SaveNewGoal());
        add(saveButton);

        setVisible(true);
    }

    private void SaveNewGoal() {
        String nameText = name.getText();
        String targetText = target.getText();
        String currentText = current.getText();

        if (nameText.isEmpty() || targetText.isEmpty()) {
            Utilities.showErrorMessage("Please fill in all fields.");
            return;
        }

        if(currentText.isEmpty()){currentText = "0.00";}

        double targetValue = Utilities.parseDouble(targetText);
        double currentValue = Utilities.parseDouble(currentText);

        if (targetValue <= 0 || currentValue < 0) {
            Utilities.showErrorMessage("Invalid target or current.");
            return;
        }

        DatabaseManager.update("INSERT INTO goals (name, target, current) VALUES (?, ?, ?)",
                nameText, targetText, currentText);

        JOptionPane.showMessageDialog(this, "Goal saved successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        goals.refresh();
    }
}
