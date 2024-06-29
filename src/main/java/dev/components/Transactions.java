package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.ModernScrollPane;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Transactions extends JPanel {
  private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
  private final ModernScrollPane scrollPane;

  public Transactions() {
    setBackground(Color.WHITE);

    setLayout(null);

    JLabel title = new JLabel("Transactions");
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

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
        try {
          row[0] = displayFormat.format(df.parse(date));
        } catch (Exception e) {
          row[0] = date;
        }
      }

      if (!row[2].toString().startsWith("R$")) {
        double amount = Utilities.parseDouble(row[2].toString());
        row[2] = String.format("R$ %.2f", amount);
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
}
