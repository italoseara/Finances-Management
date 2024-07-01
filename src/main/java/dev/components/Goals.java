package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Goals extends JPanel {
  private final ModernScrollPane scrollPane;

  public Goals() {
    setBackground(Color.WHITE);

    setLayout(null);

    JLabel title = new JLabel("Goals");
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    DBTable table = DatabaseManager.queryAsTable("SELECT name, target, current FROM goals");
    assert table != null;

    table.addColumn((Object[] row) -> {
      double target = Utilities.parseDouble(row[1].toString());
      double current = Utilities.parseDouble(row[2].toString());
      return Math.max(0, Math.min(1, current / target));
    });

    scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] {"Name", "Target", "Current", "Status"});
    scrollPane.setColumnsWidth(new double[] {.25, .25, .25, .25});
    scrollPane.setColumnsFormat((Object[] row) -> {
      if (!row[1].toString().startsWith("R$")) {
        double target = Utilities.parseDouble(row[1].toString());
        row[1] = Utilities.formatCurrency(target);
      }

      if (!row[2].toString().startsWith("R$")) {
        double current = Utilities.parseDouble(row[2].toString());
        row[2] = Utilities.formatCurrency(current);
      }

      if (!row[3].toString().endsWith("%")) {
        double left = (double) row[3];
        row[3] = "%.0f%%".formatted(left * 100);
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
