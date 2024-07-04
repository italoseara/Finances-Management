package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.style.RoundButton;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Budgets extends JPanel {
  private final JLabel title;
  private final ModernScrollPane scrollPane;

  private final RoundButton addButton;
  private final RoundButton updateButton;
  private final RoundButton removeButton;

  public Budgets() {
    setBackground(Color.WHITE);

    setLayout(null);

    int entries = DatabaseManager.queryAsInt("SELECT COUNT(*) FROM categories;");
    title = new JLabel("Monthly Budgets (%d)".formatted(entries));
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    addButton = new RoundButton("Add New", 10);
    addButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    addButton.setBackground(Color.WHITE);
    addButton.setForeground(new Color(0x111827));
    addButton.setHoverColor(new Color(0xf8f4f4));
    addButton.setBorderColor(new Color(0xe5e5e8));
    addButton.setBounds(0, 20, 135, 35);
    addButton.addActionListener(e -> new BudgetsModal(this));
    add(addButton);

    updateButton = new RoundButton("Update Selected", 10);
    updateButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    updateButton.setBackground(Color.WHITE);
    updateButton.setForeground(new Color(0x111827));
    updateButton.setHoverColor(new Color(0xf8f4f4));
    updateButton.setBorderColor(new Color(0xe5e5e8));
    updateButton.setBounds(0, 20, 185, 35);
    updateButton.addActionListener(e -> onUpdate());
    add(updateButton);

    removeButton = new RoundButton("Remove Selected", 10);
    removeButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    removeButton.setBackground(Color.WHITE);
    removeButton.setForeground(new Color(0x111827));
    removeButton.setHoverColor(new Color(0xf8f4f4));
    removeButton.setBorderColor(new Color(0xe5e5e8));
    removeButton.setBounds(0, 20, 185, 35);
    removeButton.addActionListener(e -> onRemove());
    add(removeButton);

    DBTable table = DatabaseManager.queryAsTable("SELECT id, name, budget, spent FROM categories;");
    assert table != null;

    // Add a new column to the table
    table.addColumn((Object[] row) -> {
      double budget = Utilities.parseDouble(row[2].toString());
      double spent = Utilities.parseDouble(row[3].toString());
      return Math.max(0, Math.min(1, (budget - spent) / budget));
    });

    scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] {"ID", "Category", "Budget", "Spent", "Remaining"});
    scrollPane.setColumnsWidth(new double[] {0.07, 0.3, 0.2, 0.2, 0.2});
    scrollPane.setColumnsFormat((Object[] row) -> {
      if (!row[2].toString().startsWith("R$")) {
        double budget = Utilities.parseDouble(row[2].toString());
        row[2] = Utilities.formatCurrency(budget);
      }

      if (!row[3].toString().startsWith("R$")) {
        double spent = Utilities.parseDouble(row[3].toString());
        row[3] = Utilities.formatCurrency(spent);
      }

      if (!row[4].toString().endsWith("%")) {
        double remaining = (double) row[4];
        row[4] = "%.0f%%".formatted(remaining * 100);
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
    addButton.setBounds(width - 160, 20, 135, 35);
    updateButton.setBounds(width - 350, 20, 185, 35);
    removeButton.setBounds(width - 540, 20, 185, 35);
  }

  private void onUpdate() {
    DBTable table = scrollPane.getTable();
    int[] selectedRows = table.getSelectedRows();
    if (selectedRows.length != 1) {
      return;
    }

    int id = (int) table.getValueAt(selectedRows[0], 0);
    String category = table.getValueAt(selectedRows[0], 1).toString();
    String budget = Utilities.unformattedCurrency(table.getValueAt(selectedRows[0], 2).toString());
    String spent = Utilities.unformattedCurrency(table.getValueAt(selectedRows[0], 3).toString());

    new BudgetsModal(this, id, category, budget, spent);
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
        DELETE FROM categories WHERE id IN (%s);
        """.formatted(String.join(", ", ids)));
    refresh();
  }

  public void refresh() {
    int entries = DatabaseManager.queryAsInt("SELECT COUNT(*) FROM categories;");
    title.setText("Monthly Budgets (%d)".formatted(entries));
    scrollPane.refresh();
  }
}
