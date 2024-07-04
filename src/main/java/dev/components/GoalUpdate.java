package dev.components;

import dev.manager.DatabaseManager;
import dev.style.RoundButton;
import dev.style.RoundTextField;
import dev.util.Utilities;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalUpdate extends JDialog {
    private final RoundTextField name;
    private final RoundTextField current;

    private final Goals goals;

    public GoalUpdate(Goals goals){
        this.goals = goals;

        setTitle("Update Goal");
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

        // Current label (right above the current field)
        JLabel currentLabel = new JLabel("Current:");
        currentLabel.setBounds(20, 87, 360, 30);
        currentLabel.setFont(font);
        add(currentLabel);

        // Current field
        current = new RoundTextField(10, 10, 10);
        current.setBounds(20, 117, 360, 37);
        current.setFont(font);
        current.setBackground(Color.WHITE);
        current.setForeground(new Color(0x111827));
        current.setBorderColor(new Color(0xe5e5e8));
        current.setPlaceholder("Current");
        current.setPlaceholderColor(new Color(0x6b7280));
        add(current);

        // update button
        RoundButton updateButton = new RoundButton("Save Goal", 10);
        updateButton.setFont(font);
        updateButton.setBackground(Color.WHITE);
        updateButton.setForeground(new Color(0x111827));
        updateButton.setHoverColor(new Color(0xf8f4f4));
        updateButton.setBorderColor(new Color(0xe5e5e8));
        updateButton.setBounds(20, 270, 360, 37);
        updateButton.addActionListener(e -> UpdateGoal());
        add(updateButton);

        setVisible(true);
    }

    private void UpdateGoal() {
        String nameText = name.getText();
        String currentText = current.getText();

        if (nameText.isEmpty()) {
            Utilities.showErrorMessage("Please insert a valid name.");
            return;
        }

        if(currentText.isEmpty()){currentText = "0.00";}

        double currentValue = Utilities.parseDouble(currentText);

        if (currentValue < 0) {
            Utilities.showErrorMessage("Invalid current value.");
            return;
        }
        //Não consegui fazer tratamento de erro aqui

        DatabaseManager.update(
                "UPDATE goals SET current = ? WHERE name = ?", currentValue, nameText);

        JOptionPane.showMessageDialog(this, "Goal updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        goals.refresh();
    }
}