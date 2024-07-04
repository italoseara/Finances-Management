package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.RoundButton;
import dev.style.RoundComboBox;
import dev.style.RoundTextField;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.Objects;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TransactionsModal extends JDialog {
  private final RoundTextField date;
  private final RoundTextField description;
  private final RoundTextField amount;
  private final RoundComboBox category;

  private final Transactions transactions;

  private final int id;

  public TransactionsModal(Transactions transactions, int id, String dateText,
                           String descriptionText, String amountText, String categoryText) {
    this.transactions = transactions;
    this.id = id;

    setTitle(id == -1 ? "New transaction" : "Update transaction");
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

    Font font = FontManager.getFont("Inter", Font.PLAIN, 14);

    // Date label (right above the date picker)
    JLabel dateLabel = new JLabel("Date:");
    dateLabel.setBounds(20, 20, 360, 30);
    dateLabel.setFont(font);
    add(dateLabel);

    // Date picker
    date = new RoundTextField(10, 10, 10);
    date.setBounds(20, 50, 360, 37);
    date.setFont(font);
    date.setBackground(Color.WHITE);
    date.setForeground(new Color(0x111827));
    date.setBorderColor(new Color(0xe5e5e8));
    date.setPlaceholder("Enter the date (DD/MM/YYYY)");
    date.setPlaceholderColor(new Color(0x6b7280));
    date.setText(!dateText.isEmpty() ? dateText : Utilities.formatDate(new Date()));
    add(date);

    // Description label (right above the text field)
    JLabel descriptionLabel = new JLabel("Description:");
    descriptionLabel.setBounds(20, 87, 360, 30);
    descriptionLabel.setFont(font);
    add(descriptionLabel);

    // Description text field
    description = new RoundTextField(10, 10, 10);
    description.setFont(font);
    description.setBackground(Color.WHITE);
    description.setForeground(new Color(0x111827));
    description.setBorderColor(new Color(0xe5e5e8));
    description.setBounds(20, 117, 360, 37);
    description.setPlaceholder("Enter the description");
    description.setPlaceholderColor(new Color(0x6b7280));
    description.setText(descriptionText);
    add(description);

    // Amount label (right above the text field)
    JLabel amountLabel = new JLabel("Amount:");
    amountLabel.setBounds(20, 154, 360, 30);
    amountLabel.setFont(font);
    add(amountLabel);

    // Amount text field
    amount = new RoundTextField(10, 10, 10);
    amount.setBounds(20, 184, 360, 37);
    amount.setFont(font);
    amount.setBackground(Color.WHITE);
    amount.setForeground(new Color(0x111827));
    amount.setBorderColor(new Color(0xe5e5e8));
    amount.setPlaceholder("Enter the amount (e.g. 100.00)");
    amount.setPlaceholderColor(new Color(0x6b7280));
    amount.setText(amountText);
    add(amount);

    // Category label (right above the combo box)
    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setBounds(20, 221, 360, 30);
    categoryLabel.setFont(font);
    add(categoryLabel);

    // Category combo box
    category = getCategoryComboBox();
    category.setForeground(new Color(0x111827));
    category.setBackground(Color.WHITE);
    category.setBorderColor(new Color(0xe5e5e8));
    category.setBounds(20, 251, 360, 37);
    category.setFont(font);
    category.setSelectedItem(categoryText);
    add(category);

    // Button to save the transaction
    RoundButton saveButton =
        new RoundButton(id == -1 ? "Save Transaction" : "Update Transaction", 10, 20, 10);
    saveButton.setFont(font);
    saveButton.setBackground(Color.WHITE);
    saveButton.setForeground(new Color(0x111827));
    saveButton.setHoverColor(new Color(0xf8f4f4));
    saveButton.setBorderColor(new Color(0xe5e5e8));
    saveButton.setBounds(20, 315, 360, 37);
    saveButton.addActionListener(e -> onButtonClick());
    add(saveButton);

    setVisible(true);
  }

  public TransactionsModal(Transactions transactions) {
    this(transactions, -1, "", "", "", "");
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
    if (amountValue == 0) {
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
    if (id != -1) {
      DatabaseManager.update(
          "UPDATE transactions SET date = ?, description = ?, amount = ?, category_id = ? WHERE id = ?;",
          dateTextUnformatted, descriptionText, amountValue, categoryId, id);
      JOptionPane.showMessageDialog(null, "Transaction updated successfully.", "Success",
          JOptionPane.INFORMATION_MESSAGE);
    } else {
      DatabaseManager.update(
          "INSERT INTO transactions (date, description, amount, category_id) VALUES (?, ?, ?, ?);",
          dateTextUnformatted, descriptionText, amountValue, categoryId);
      JOptionPane.showMessageDialog(null, "Transaction saved successfully.", "Success",
          JOptionPane.INFORMATION_MESSAGE);
    }

    dispose();
    transactions.refresh();
    Dashboard.getInstance().refresh();
  }
}
