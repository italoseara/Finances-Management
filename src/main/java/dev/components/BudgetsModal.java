package dev.components;

import dev.manager.DatabaseManager;
import dev.style.RoundButton;
import dev.style.RoundComboBox;
import dev.style.RoundTextField;
import dev.util.Utilities;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

public class BudgetsModal extends JDialog {
    private final RoundComboBox category;
    private final RoundTextField budget;
    private final RoundTextField spent;

    private final Budgets budgets;

    public BudgetsModal(Budgets budgets){
        this.budgets = budgets;

        setTitle("New budget");
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
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(20, 20, 360, 30);
        categoryLabel.setFont(font);
        add(categoryLabel);

        // Category combo box
        category = getCategoryComboBox();
        category.setBounds(20, 50, 360, 37);
        category.setFont(font);
        category.setBackground(Color.WHITE);
        category.setForeground(new Color(0x111827));
        category.setBorderColor(new Color(0xe5e5e8));
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
        budget.setPlaceholder("R$ 0.00");
        budget.setPlaceholderColor(new Color(0x6b7280));
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
        spent.setPlaceholder("R$ 0.00");
        spent.setPlaceholderColor(new Color(0x6b7280));
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

    private void onButtonClick() {
        String categoryText = Objects.requireNonNull(category.getSelectedItem()).toString();
        String budgetText = budget.getText();
        String spentText = spent.getText();

        if (categoryText.isEmpty() || budgetText.isEmpty()) {
            Utilities.showErrorMessage("Please fill all fields.");
            return;
        }

        if(spentText.isEmpty()){spentText = "0.00";}

        double budgetValue = Utilities.parseDouble(budgetText);
        double spentValue = Utilities.parseDouble(spentText);

        if (budgetValue < 0 || spentValue < 0) {
            Utilities.showErrorMessage("Invalid amount or spent value.");
            return;
        }

        int categoryId =
                DatabaseManager.queryAsInt("SELECT id FROM categories WHERE name = ?;", categoryText);
        if (categoryId == -1) {
            Utilities.showErrorMessage("Category not found.");
            return;
        }

        // Change the values in the database
        DatabaseManager.update(
                "UPDATE categories SET budget = ?, spent = ? WHERE id = ?;",
                budgetValue, spentValue, categoryId);

        JOptionPane.showMessageDialog(null, "Budget updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        budgets.refresh();
    }

    private RoundComboBox getCategoryComboBox() {
        String[] categories = DatabaseManager.queryAsArray("SELECT name FROM categories;");
        if (categories == null) {
            Utilities.showErrorMessage("No categories found.");
            dispose();
            return new RoundComboBox(10);
        }

        return new RoundComboBox(categories, 10, 10, 10);
    }
}
