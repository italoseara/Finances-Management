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

public class Goals extends JPanel {
  private final ModernScrollPane scrollPane;

  private final RoundButton newGoalButton;
  private final RoundButton updateGoalButton;
  private final RoundButton deleteGoalButton;


  public Goals() {
    setBackground(Color.WHITE);

    setLayout(null);

    JLabel title = new JLabel("Goals");
    title.setFont(FontManager.getFont("Inter", Font.BOLD, 24));
    title.setBounds(20, 20, 500, 30);
    title.setForeground(new Color(0x111827));
    add(title);

    newGoalButton = new RoundButton("New Goal", 10);
    newGoalButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    newGoalButton.setBackground(Color.WHITE);
    newGoalButton.setForeground(new Color(0x111827));
    newGoalButton.setHoverColor(new Color(0xf8f4f4));
    newGoalButton.setBorderColor(new Color(0xe5e5e8));
    newGoalButton.setBounds(0, 20, 135, 35);
    newGoalButton.addActionListener(e -> new GoalsModal(this));
    add(newGoalButton);

    updateGoalButton = new RoundButton("Update Goals", 10);
    updateGoalButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    updateGoalButton.setBackground(Color.WHITE);
    updateGoalButton.setForeground(new Color(0x111827));
    updateGoalButton.setHoverColor(new Color(0xf8f4f4));
    updateGoalButton.setBorderColor(new Color(0xe5e5e8));
    updateGoalButton.setBounds(0, 20, 135, 35);
    updateGoalButton.addActionListener(e -> new GoalUpdate(this));
    add(updateGoalButton);

    deleteGoalButton = new RoundButton("Delete Goal", 10);
    deleteGoalButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    deleteGoalButton.setBackground(Color.WHITE);
    deleteGoalButton.setForeground(new Color(0x111827));
    deleteGoalButton.setHoverColor(new Color(0xf8f4f4));
    deleteGoalButton.setBorderColor(new Color(0xe5e5e8));
    deleteGoalButton.setBounds(0, 20, 135, 35);
    deleteGoalButton.addActionListener(e -> onRemove());
    add(deleteGoalButton);

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

  private void onRemove() {
    DBTable table = scrollPane.getTable();
    int[] selectedRows = table.getSelectedRows();
    if (selectedRows.length == 0) {
      return;
    }
    // FUNCIONA MAS TBM PERDE A FORMATAÇÂO
    for (int row : selectedRows) {
      String name = table.getValueAt(row, 0).toString();
      DatabaseManager.update("DELETE FROM goals WHERE name = ?;", name); //Por usar o name como "chave"
    }                                                                          //não é possivel deletar mais de um de uma vez

    refresh();
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    scrollPane.setBounds(20, 70, width - 40, height - 90);
    newGoalButton.setBounds(width - 160, 20, 135, 35);
    updateGoalButton.setBounds(width - 300, 20, 135, 35);
    deleteGoalButton.setBounds(width - 440, 20, 135, 35);
  }

  public void refresh() {
    scrollPane.refresh();
  }
}
