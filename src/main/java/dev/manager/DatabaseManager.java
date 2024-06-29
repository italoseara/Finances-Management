package dev.manager;

import dev.util.SwingUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DatabaseManager {
  private static Connection connection;

  public static void connect(String filename) {
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
    } catch (SQLException e) {
      SwingUtil.showErrorMessage(e.getMessage());
      return;
    }

    if (connection == null) {
      SwingUtil.showErrorMessage("Failed to connect to the database.");
      return;
    }

    if (!createTables()) {
      SwingUtil.showErrorMessage("Failed to create tables.");
      return;
    }

    populateWithFakeData();
  }

  private static boolean createTables() {
    try {
      Statement statement = connection.createStatement();

      statement.execute("""
          CREATE TABLE IF NOT EXISTS categories (
              id     INTEGER PRIMARY KEY AUTOINCREMENT,
              name   VARCHAR(100) NOT NULL UNIQUE,
              budget REAL NOT NULL DEFAULT 0,
              spent  REAL NOT NULL DEFAULT 0
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
      SwingUtil.showErrorMessage(e.getMessage());
      return false;
    }

    return true;
  }

  public static void populateWithFakeData() {
    // Populate the categories table with fake data
    try {
      Statement statement = connection.createStatement();

      ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM categories");
      result.next();
      if (result.getInt(1) > 0) { // If the table is not empty, return
        return;
      }
      result.close();

      Random random = new Random();
      String[] categories = {"Food", "Transportation", "Entertainment", "Health", "Education"};

      // Insert fake data into the categories table
      for (String category : categories) {
        statement.execute("INSERT INTO categories (name, budget) VALUES ('" + category + "', " +
            (random.nextInt(1000) + 100) + ");");
      }

      // Insert fake data into the transactions table
      for (int i = 0; i < 100; i++) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        String amount = df2.format(random.nextDouble() * 1000);
        int categoryId = random.nextInt(categories.length) + 1;

        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        String date = String.format("2024-%02d-%02d 00:00:00", month, day);

        statement.execute(String.format("""
            INSERT INTO transactions (date, description, amount, category_id)
            VALUES ('%s', 'Transaction #%d', %s, %d);""", date, i + 1, amount, categoryId));
      }

      statement.close();
    } catch (SQLException e) {
      SwingUtil.showErrorMessage(e.getMessage());
    }
  }

  public static JTable asTable(String query) {
    try {
      DefaultTableModel model = new DefaultTableModel();

      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(query);
      ResultSetMetaData resultMD = result.getMetaData();
      int columns = resultMD.getColumnCount();

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

      result.close();
      statement.close();

      // Create the JTable with the model
      return new JTable(model);
    } catch (SQLException e) {
      SwingUtil.showErrorMessage(e.getMessage());
      return null;
    }
  }
}