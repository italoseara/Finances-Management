package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.style.RoundButton;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLOutput;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Budgets extends JPanel {
  private final JLabel title;
  private final ModernScrollPane scrollPane;

  private final RoundButton changeButton;

  public Budgets() {
    setBackground(Color.WHITE);

    setLayout(null);

    title = new JLabel("Monthly Budgets");
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    // Add a new budget
    changeButton = new RoundButton("Update Bills", 10);
    changeButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    changeButton.setBackground(Color.WHITE);
    changeButton.setForeground(new Color(0x111827));
    changeButton.setHoverColor(new Color(0xf8f4f4));
    changeButton.setBorderColor(new Color(0xe5e5e8));
    changeButton.setBounds(0, 20, 135, 35);
    changeButton.addActionListener(e -> new BudgetsModal(this));
    add(changeButton);

    DBTable table = DatabaseManager.queryAsTable("SELECT name, budget, spent FROM categories;");
    assert table != null;

    // Add a new column to the table
    table.addColumn((Object[] row) -> {
      double budget = Utilities.parseDouble(row[1].toString());
      double spent = Utilities.parseDouble(row[2].toString());
      return Math.max(0, Math.min(1, (budget - spent) / budget));
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
    changeButton.setBounds(width - 160, 20, 135, 35);
  }

  public void refresh() {
    scrollPane.refresh(); //Aparentemente o problema tá aqui
    //System.out.println("Refreshed"); // Sequer printa essa linha
    // Talvez o erro esteja relacionado a coluna Remaining não existir no banco de dados
  }

}
