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

public class BudgetsModal extends JDialog {
  private final RoundTextField category;
  private final RoundTextField budget;
  private final RoundTextField spent;

  private final Budgets budgets;

  private final int id;

  public BudgetsModal(Budgets budgets, int id, String categoryText, String budgetText,
                      String spentText) {
    this.budgets = budgets;
    this.id = id;

    setTitle(id == -1 ? "New category" : "Update category");
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

    // Category label (right above the category combo box)
    JLabel categoryLabel = new JLabel("Name:");
    categoryLabel.setBounds(20, 20, 360, 30);
    categoryLabel.setFont(font);
    add(categoryLabel);

    // Category combo box
    category = new RoundTextField(10, 10, 10);
    category.setBounds(20, 50, 360, 37);
    category.setFont(font);
    category.setBackground(Color.WHITE);
    category.setForeground(new Color(0x111827));
    category.setBorderColor(new Color(0xe5e5e8));
    category.setPlaceholder("Enter the category name");
    category.setText(categoryText);
    add(category);

    // Budget label
    JLabel budgetLabel = new JLabel("Budget:");
    budgetLabel.setBounds(20, 100, 360, 30);
    budgetLabel.setFont(font);
    add(budgetLabel);

    // Budget text field
    budget = new RoundTextField(10, 10, 10);
    budget.setBounds(20, 130, 360, 37);
    budget.setFont(font);
    budget.setBackground(Color.WHITE);
    budget.setForeground(new Color(0x111827));
    budget.setBorderColor(new Color(0xe5e5e8));
    budget.setPlaceholder("Enter the budget amount (R$)");
    budget.setPlaceholderColor(new Color(0x6b7280));
    budget.setText(budgetText);
    add(budget);

    // Spent label
    JLabel spentLabel = new JLabel("Spent:");
    spentLabel.setBounds(20, 180, 360, 30);
    spentLabel.setFont(font);
    add(spentLabel);

    // Spent text field
    spent = new RoundTextField(10, 10, 10);
    spent.setBounds(20, 210, 360, 37);
    spent.setFont(font);
    spent.setBackground(Color.WHITE);
    spent.setForeground(new Color(0x111827));
    spent.setBorderColor(new Color(0xe5e5e8));
    spent.setPlaceholder("Enter the spent amount (R$)");
    spent.setPlaceholderColor(new Color(0x6b7280));
    spent.setText(spentText);
    add(spent);

    // Save button
    RoundButton saveButton = new RoundButton("Update Budget", 10);
    saveButton.setFont(font);
    saveButton.setBackground(Color.WHITE);
    saveButton.setForeground(new Color(0x111827));
    saveButton.setHoverColor(new Color(0xf8f4f4));
    saveButton.setBorderColor(new Color(0xe5e5e8));
    saveButton.setBounds(20, 265, 360, 37);
    saveButton.addActionListener(e -> onButtonClick());
    add(saveButton);

    setVisible(true);
  }

  public BudgetsModal(Budgets budgets) {
    this(budgets, -1, "", "", "");
  }

  private void onButtonClick() {
    String categoryText = category.getText();
    String budgetText = budget.getText();
    String spentText = spent.getText();

    if (categoryText.isEmpty() || budgetText.isEmpty()) {
      Utilities.showErrorMessage("Please fill all fields.");
      return;
    }

    if (spentText.isEmpty()) {
      spentText = "0.00";
    }

    double budgetValue = Utilities.parseDouble(budgetText);
    double spentValue = Utilities.parseDouble(spentText);

    if (budgetValue < 0 || spentValue < 0) {
      Utilities.showErrorMessage("Invalid amount or spent value.");
      return;
    }

    if (id == -1) {
      DatabaseManager.update("INSERT INTO categories (name, budget, spent) VALUES (?, ?, ?);",
          categoryText, budgetValue, spentValue);
      JOptionPane.showMessageDialog(null, "Category added successfully.", "Success",
          JOptionPane.INFORMATION_MESSAGE);
    } else {
      DatabaseManager.update("UPDATE categories SET name = ?, budget = ?, spent = ? WHERE id = ?;",
          categoryText, budgetValue, spentValue, id);
      JOptionPane.showMessageDialog(null, "Category updated successfully.", "Success",
          JOptionPane.INFORMATION_MESSAGE);
    }

    dispose();
    budgets.refresh();
  }
}
