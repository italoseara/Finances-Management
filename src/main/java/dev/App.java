package dev;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;

public class App extends JFrame {
  private String tableName = "transactions";
  private final Connection connection;

  public App() {
    if ((connection = connect()) == null || !createTable()) {
      throw new RuntimeException("Failed to connect to the database.");
    }
  }

  private Connection connect() {
    Connection connection;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    return connection;
  }

  private boolean createTable() {
    try {
      String sql = String.format("""
          CREATE TABLE IF NOT EXISTS %s (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            date INTEGER DEFAULT CURRENT_TIMESTAMP,
            description VARCHAR(100) NOT NULL,
            amount REAL NOT NULL,
            category VARCHAR(100) NOT NULL
          );""", tableName);

      Statement statement = connection.createStatement();
      statement.execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }
}
