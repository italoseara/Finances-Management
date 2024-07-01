package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
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

    JTable table = DatabaseManager.asTable("SELECT name, budget, spent FROM categories;");
    assert table != null;

    // Add a new column to the table
    TableColumn remainingColumn = new TableColumn();
    table.addColumn(remainingColumn);
    remainingColumn.setHeaderValue("Remaining");

    var model = (DefaultTableModel) table.getModel();

    // Calculate the remaining budget for each category
    for (int i = 0; i < table.getRowCount(); i++) {
      String budgetString = Objects.requireNonNull(table.getValueAt(i, 1)).toString();
      String spentString = Objects.requireNonNull(table.getValueAt(i, 2)).toString();
      double budget = Utilities.parseDouble(budgetString);
      double spent = Utilities.parseDouble(spentString);
      double remaining = (budget - spent) / budget;

      Vector<Double> row = model.getDataVector().elementAt(i);
      row.add(remaining);
    }

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
