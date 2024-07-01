package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.style.RoundedButton;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Transactions extends JPanel {
  private final JLabel title;
  private final ModernScrollPane scrollPane;

  private final RoundedButton addButton;
  private final RoundedButton removeButton;

  public Transactions() {
    setBackground(Color.WHITE);

    setLayout(null);

    int entries = DatabaseManager.queryAsInt("SELECT COUNT(*) FROM transactions;");
    title = new JLabel("Transactions (%d)".formatted(entries));
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    addButton = new RoundedButton("Add New", 10);
    addButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    addButton.setBackground(Color.WHITE);
    addButton.setForeground(new Color(0x111827));
    addButton.setHoverColor(new Color(0xf8f4f4));
    addButton.setBorderColor(new Color(0xe5e5e8));
    addButton.setBounds(0, 20, 135, 35);
    addButton.addActionListener(e -> new TransactionsModal(this));
    add(addButton);

    removeButton = new RoundedButton("Remove Selected", 10);
    removeButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    removeButton.setBackground(Color.WHITE);
    removeButton.setForeground(new Color(0x111827));
    removeButton.setHoverColor(new Color(0xf8f4f4));
    removeButton.setBorderColor(new Color(0xe5e5e8));
    removeButton.setBounds(0, 20, 185, 35);
    removeButton.addActionListener(e -> onRemove());
    add(removeButton);

    DBTable table = DatabaseManager.queryAsTable("""
        SELECT transactions.id, date, description, amount, name FROM transactions
        JOIN categories ON transactions.category_id = categories.id
        ORDER BY date DESC;
        """);

    scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] {"ID", "Date", "Description", "Amount", "Category"});
    scrollPane.setColumnsWidth(new double[] {.07, .15, .4, .15, .23});
    scrollPane.setColumnsFormat((Object[] row) -> {
      if (row[1] instanceof String date) {
        row[1] = Utilities.formatDate(date);
      }

      if (!row[3].toString().startsWith("R$")) {
        double amount = Utilities.parseDouble(row[3].toString());
        row[3] = Utilities.formatCurrency(amount);
      }

      return row;
    });

    scrollPane.setBounds(20, 70, 760, 680);
    add(scrollPane);
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    scrollPane.setBounds(20, 70, width - 40, height - 90);
    addButton.setBounds(width - 160, 20, 135, 35);
    removeButton.setBounds(width - 355, 20, 185, 35);
  }

  private void onRemove() {
    DBTable table = scrollPane.getTable();
    int[] selectedRows = table.getSelectedRows();
    if (selectedRows.length == 0) {
      return;
    }

    String[] ids = new String[selectedRows.length];
    for (int i = 0; i < selectedRows.length; i++) {
      ids[i] = String.valueOf(table.getValueAt(selectedRows[i], 0));
    }

    DatabaseManager.update("""
        DELETE FROM transactions WHERE id IN (%s);
        """.formatted(String.join(", ", ids)));
    refresh();
  }

  public void refresh() {
    int entries = DatabaseManager.queryAsInt("SELECT COUNT(*) FROM transactions;");
    title.setText("Transactions (%d)".formatted(entries));
    scrollPane.refresh();
  }
}
