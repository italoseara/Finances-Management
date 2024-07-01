package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Budgets extends JPanel {
  private final ModernScrollPane scrollPane;

  @SuppressWarnings("unchecked")
  public Budgets() {
    setBackground(Color.WHITE);

    setLayout(null);

    JLabel title = new JLabel("Monthly Budgets");
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    DBTable table = DatabaseManager.queryAsTable("SELECT name, budget, spent FROM categories;");
    assert table != null;

    // Add a new column to the table
    table.addColumn((Object[] row) -> {
      double budget = Utilities.parseDouble(row[1].toString());
      double spent = Utilities.parseDouble(row[2].toString());
      return Math.max(0, Math.min(1, spent / budget));
    });

    scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] {"Category", "Budget", "Spent", "Remaining"});
    scrollPane.setColumnsWidth(new double[] {.25, .25, .25, .25});
    scrollPane.setColumnsFormat((Object[] row) -> {
      if (!row[1].toString().startsWith("R$")) {
        double budget = Utilities.parseDouble(row[1].toString());
        row[1] = Utilities.formatCurrency(budget);
      }

      if (!row[2].toString().startsWith("R$")) {
        double spent = Utilities.parseDouble(row[2].toString());
        row[2] = Utilities.formatCurrency(spent);
      }

      if (!row[3].toString().endsWith("%")) {
        double remaining = (double) row[3];
        row[3] = "%.0f%%".formatted(remaining * 100);
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
