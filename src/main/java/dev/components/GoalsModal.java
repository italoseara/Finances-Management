package dev.components;

import dev.manager.DatabaseManager;
import dev.style.RoundButton;
import dev.style.RoundTextField;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GoalsModal extends JDialog {
  private final RoundTextField name;
  private final RoundTextField target;
  private final RoundTextField current;

  private final Goals goals;

  private final int id;

  public GoalsModal(Goals goals, int id, String nameText, String targetValue, String currentValue) {
    this.goals = goals;
    this.id = id;

    setTitle(id == -1 ? "New goal" : "Update goal");
    boolean isWindows = Utilities.isWindows();
    setSize(400 + (isWindows ? 16 : 0), 330 + (isWindows ? 39 : 0)); // Windows fix
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
    name.setPlaceholder("Enter the name");
    name.setPlaceholderColor(new Color(0x6b7280));
    name.setText(nameText);
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
    target.setPlaceholder("Enter the target (e.g. 100.00)");
    target.setPlaceholderColor(new Color(0x6b7280));
    target.setText(targetValue);
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
    current.setPlaceholder("Enter the current value (e.g. 50.00)");
    current.setPlaceholderColor(new Color(0x6b7280));
    current.setText(currentValue);
    add(current);

    // Save button
    RoundButton saveButton = new RoundButton(id == -1 ? "Save Goal" : "Update Goal", 10);
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

  public GoalsModal(Goals goals) {
    this(goals, -1, "", "", "");
  }

  private void SaveNewGoal() {
    String nameText = name.getText();
    String targetText = target.getText();
    String currentText = current.getText();

    if (nameText.isEmpty() || targetText.isEmpty()) {
      Utilities.showErrorMessage("Please fill in all fields.");
      return;
    }

    if (currentText.isEmpty()) {
      currentText = "0.00";
    }

    double targetValue = Utilities.parseDouble(targetText);
    double currentValue = Utilities.parseDouble(currentText);

    if (targetValue <= 0 || currentValue < 0) {
      Utilities.showErrorMessage("Invalid target or current.");
      return;
    }

    dispose();
    goals.refresh();

    if (id != -1) {
      DatabaseManager.update("UPDATE goals SET name = ?, target = ?, current = ? WHERE id = ?",
          nameText, targetText, currentText, id);

      JOptionPane.showMessageDialog(this, "Goal updated successfully.", "Success",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    DatabaseManager.update("INSERT INTO goals (name, target, current) VALUES (?, ?, ?)", nameText,
        targetText, currentText);

    JOptionPane.showMessageDialog(this, "Goal saved successfully.", "Success",
        JOptionPane.INFORMATION_MESSAGE);
  }
}
