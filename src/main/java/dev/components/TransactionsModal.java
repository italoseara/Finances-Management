package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.RoundedButton;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.Objects;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TransactionsModal extends JDialog {
  private final JTextField date;
  private final JTextField description;
  private final JTextField amount;
  private final JComboBox<String> category;

  private final Font font;

  private final Transactions transactions;

  public TransactionsModal(Transactions transactions) {
    this.transactions = transactions;

    setTitle("New transaction");
    setSize(400, 350);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setModal(true);
    setLayout(null);

    // Modal styling
    setResizable(false);
    setBackground(Color.WHITE);
    getContentPane().setBackground(Color.WHITE);

    font = FontManager.getFont("Inter", Font.PLAIN, 14);

    // Date label (right above the date picker)
    JLabel dateLabel = new JLabel("Date:");
    dateLabel.setBounds(20, 20, 360, 30);
    dateLabel.setFont(font);
    add(dateLabel);

    // Date picker
    date = new JTextField();
    date.setBounds(20, 50, 360, 30);
    date.setFont(font);
    date.setText(Utilities.formatDate(new Date()));
    add(date);

    // Description label (right above the text field)
    JLabel descriptionLabel = new JLabel("Description:");
    descriptionLabel.setBounds(20, 80, 360, 30);
    descriptionLabel.setFont(font);
    add(descriptionLabel);

    // Description text field
    description = new JTextField();
    description.setBounds(20, 110, 360, 30);
    description.setFont(font);
    add(description);

    // Amount label (right above the text field)
    JLabel amountLabel = new JLabel("Amount:");
    amountLabel.setBounds(20, 140, 360, 30);
    amountLabel.setFont(font);
    add(amountLabel);

    // Amount text field
    amount = new JTextField();
    amount.setBounds(20, 170, 360, 30);
    amount.setFont(font);
    add(amount);

    // Category label (right above the combo box)
    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setBounds(20, 200, 360, 30);
    categoryLabel.setFont(font);
    add(categoryLabel);

    // Category combo box
    category = getCategoryComboBox();
    category.setBounds(20, 230, 360, 30);
    category.setFont(font);
    add(category);

    // Button to save the transaction
    RoundedButton saveButton = new RoundedButton("Save Transaction", 10, 20, 10);
    saveButton.setFont(font);
    saveButton.setBackground(Color.WHITE);
    saveButton.setForeground(new Color(0x111827));
    saveButton.setHoverColor(new Color(0xf8f4f4));
    saveButton.setBorderColor(new Color(0xe5e5e8));
    saveButton.setBounds(20, 290, 360, 35);
    saveButton.addActionListener(e -> onButtonClick());
    add(saveButton);

    setVisible(true);
  }

  private JComboBox<String> getCategoryComboBox() {
    String[] categories = DatabaseManager.queryAsArray("SELECT name FROM categories;");
    if (categories == null) {
      Utilities.showErrorMessage("No categories found.");
      dispose();
      return new JComboBox<>();
    }

    return new JComboBox<>(categories);
  }

  private void onButtonClick() {
    String dateText = date.getText();
    String descriptionText = description.getText();
    String amountText = amount.getText();
    String categoryText = Objects.requireNonNull(category.getSelectedItem()).toString();

    if (dateText.isEmpty() || descriptionText.isEmpty() || amountText.isEmpty()) {
      Utilities.showErrorMessage("Please fill all fields.");
      return;
    }

    if (!Utilities.isValidDate(dateText)) {
      Utilities.showErrorMessage("Invalid date format.");
      return;
    }

    double amountValue = Utilities.parseDouble(amountText);
    if (amountValue <= 0) {
      Utilities.showErrorMessage("Invalid amount.");
      return;
    }

    int categoryId =
        DatabaseManager.queryAsInt("SELECT id FROM categories WHERE name = ?;", categoryText);
    if (categoryId == -1) {
      Utilities.showErrorMessage("Category not found.");
      return;
    }

    String dateTextUnformatted = Utilities.unformattedDate(dateText);
    DatabaseManager.update(
        "INSERT INTO transactions (date, description, amount, category_id) VALUES (?, ?, ?, ?);",
        dateTextUnformatted, descriptionText, amountValue, categoryId);
    dispose();
    transactions.refresh();

    JOptionPane.showMessageDialog(null, "Transaction saved successfully.", "Success",
        JOptionPane.INFORMATION_MESSAGE);
  }
}
