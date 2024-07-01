package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.Button;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
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
    button.addActionListener(e -> new TransactionsModal(this));
    add(button);

    DBTable table = DatabaseManager.queryAsTable("""
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

  public void refresh() {
    scrollPane.refresh();
  }
}
