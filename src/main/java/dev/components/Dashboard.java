package dev.components;

import dev.manager.DatabaseManager;
import dev.style.ModernScrollPane;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Dashboard extends JPanel {
  private final ModernScrollPane scrollPane;

  public Dashboard() {
    setBackground(Color.WHITE);

    // Align everything to the left, and put the items on top of each other
    setLayout(null);

    JTable table = DatabaseManager.asTable("SELECT * FROM categories;");
    scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] { "ID", "Name", "Budget" });
    scrollPane.setColumnWidths(new double[] { .07, .48, .45 });
    scrollPane.setRowFormat((Object[] row) -> {
      row[2] = String.format("R$ %.2f", (double)row[2]);
      return row;
    });

    scrollPane.setBounds(20, 20, 760, 680);
    add(scrollPane);

    add(new JLabel("Dashboard"));
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    scrollPane.setBounds(20, 20, width - 40, height - 40);
  }
}
