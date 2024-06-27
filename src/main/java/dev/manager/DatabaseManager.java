package dev.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DatabaseManager {
  private final Connection connection;

  public DatabaseManager(String filename) {
    if ((connection = connect(filename)) == null) {
      throw new RuntimeException("Failed to connect to the database.");
    }

    if (!createTables()) {
      throw new RuntimeException("Failed to create tables.");
    }

    populateWithFakeData();
  }

  private Connection connect(String filename) {
    try {
      return DriverManager.getConnection("jdbc:sqlite:" + filename);
    } catch (SQLException e) {
      return null;
    }
  }

  private boolean createTables() {
    try {
      Statement statement = connection.createStatement();

      statement.execute("""
          CREATE TABLE IF NOT EXISTS categories (
              id     INTEGER PRIMARY KEY AUTOINCREMENT,
              name   VARCHAR(100) NOT NULL UNIQUE,
              budget REAL NOT NULL DEFAULT 0
          );""");

      statement.execute("""
          CREATE TABLE IF NOT EXISTS transactions (
              id          INTEGER PRIMARY KEY AUTOINCREMENT,
              date        TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
              description VARCHAR(100) NOT NULL,
              amount      REAL NOT NULL,
              category_id INTEGER NOT NULL,
              FOREIGN KEY (category_id) REFERENCES categories(id)
          );""");

      statement.execute("""
          CREATE TABLE IF NOT EXISTS bills (
              id          INTEGER PRIMARY KEY AUTOINCREMENT,
              date        TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
              description VARCHAR(100) NOT NULL,
              amount      REAL NOT NULL,
              category_id INTEGER NOT NULL,
              FOREIGN KEY (category_id) REFERENCES categories(id)
          );""");

      statement.execute("""
          CREATE TABLE IF NOT EXISTS goals (
              id          INTEGER PRIMARY KEY AUTOINCREMENT,
              name        VARCHAR(100) NOT NULL,
              target      REAL NOT NULL,
              current     REAL NOT NULL DEFAULT 0
          );""");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public void populateWithFakeData() {
    // Populate the categories table with fake data
    try {
      Statement statement = connection.createStatement();

      ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM categories");
      result.next();
      if (result.getInt(1) > 0) { // If the table is not empty, return
        return;
      }

      statement.execute("INSERT INTO categories (name, budget) VALUES ('Food', 200)");
      statement.execute("INSERT INTO categories (name, budget) VALUES ('Transportation', 100)");
      statement.execute("INSERT INTO categories (name, budget) VALUES ('Entertainment', 50)");
      statement.execute("INSERT INTO categories (name, budget) VALUES ('Health', 100)");
      statement.execute("INSERT INTO categories (name, budget) VALUES ('Education', 50)");
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public JTable asTable(String query) {
    try {
      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(query);
      ResultSetMetaData resultMD = result.getMetaData();

      int columns = resultMD.getColumnCount();
      DefaultTableModel model = new DefaultTableModel();

      // Add columns to the model, and set their names
      for (int i = 1; i <= columns; i++) {
        model.addColumn(resultMD.getColumnName(i));
      }

      // Fill the model with the data resulting from the query
      while (result.next()) {
        Object[] row = new Object[columns];
        for (int i = 0; i < columns; i++) {
          row[i] = result.getObject(i + 1);
        }

        model.addRow(row);
      }

      return new JTable(model);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}