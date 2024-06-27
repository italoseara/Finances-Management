package dev;

import dev.manager.DatabaseManager;
import javax.swing.*;

public class App extends JFrame {
  private final DatabaseManager dbManager;

  public App() {
    super("Financial Manager");

    dbManager = new DatabaseManager("financial.db");

    JTable table = dbManager.asTable("SELECT name, budget FROM categories");
    add(new JScrollPane(table));

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setVisible(true);
    setLayout(null);
  }
}
