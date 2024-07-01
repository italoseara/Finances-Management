package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.ModernScrollPane;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Transactions extends JPanel {
  private final ModernScrollPane scrollPane;

  public Transactions() {
    setBackground(Color.WHITE);

    setLayout(null);

    JLabel title = new JLabel("Transactions");
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    Button button = new Button(720, 25, 130, 30, "New transaction");
    button.setFont(FontManager.getFont("Inter", Font.PLAIN, 12));
    button.addActionListener(e -> newTransaction());
    add(button);

    JTable table = DatabaseManager.asTable("""
        SELECT date, description, amount, name FROM transactions
        JOIN categories ON transactions.category_id = categories.id
        ORDER BY date DESC;
        """);

    scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] {"Date", "Description", "Amount", "Category"});
    scrollPane.setColumnsWidth(new double[] {.15, .47, .15, .23});
    scrollPane.setColumnsFormat((Object[] row) -> {
      if (row[0] instanceof String date) {
        row[0] = Utilities.formatDate(date);
      }

      if (!row[2].toString().startsWith("R$")) {
        double amount = Utilities.parseDouble(row[2].toString());
        row[2] = Utilities.formatCurrency(amount);
      }

      return row;
    });

    scrollPane.setBounds(20, 20, 760, 680);
    add(scrollPane);
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    scrollPane.setBounds(20, 70, width - 40, height - 90);
  }

  private void newTransaction() {
    JTextField descriptionField = new JTextField();
    JTextField amountField = new JTextField();
    JComboBox<String> categoryField = new JComboBox<>();
    categoryField.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    categoryField.addItem("Select a category");
    categoryField.addItem("Food");
    categoryField.addItem("Transportation");
    categoryField.addItem("Entertainment");
    categoryField.addItem("Health");
    categoryField.setSelectedIndex(0);

    Object[] message = {
        "Description:", descriptionField,
        "Amount:", amountField,
        "Category:", categoryField
    };

    int option = JOptionPane.showConfirmDialog(null, message, "New transaction", JOptionPane.OK_CANCEL_OPTION);
    if (option != JOptionPane.OK_OPTION) {
      return;
    }

    String description = descriptionField.getText();
    double amount = Utilities.parseDouble(amountField.getText());
    int category = categoryField.getSelectedIndex();

    //insert the new transaction into the database
    DatabaseManager.query("""
        INSERT INTO transactions (description, amount, category_id)
        VALUES ('%s', %s, %d);
        """.formatted(description, amount, category));
  }

}
