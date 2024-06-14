package dev;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class App extends JFrame {
  private String tableName = "transactions";
  private final Connection connection;

  public App() {
    super("Financial Manager");

    if ((connection = connect()) == null || !createTable()) {
      throw new RuntimeException("Failed to connect to the database.");
    }

    //Cria o modelo da tabela
    DefaultTableModel model = new DefaultTableModel();
    JTable table = populateTable(model);

    //Adiciona a tabela a um JScrollPane
    JScrollPane scrollPane = new JScrollPane(table);

    //inicializa o JFrame
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    super.setSize(800, 600);
    super.setLocationRelativeTo(null);
    super.add(scrollPane);
    super.setVisible(true);
  }

  private JTable populateTable(DefaultTableModel model) {
    try{
      //Consulta SQL pra recuperar os dados da tabela
      String sql = String.format("SELECT * FROM %s;", tableName);
      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(sql);

      ResultSetMetaData resultMD = result.getMetaData();
      int columns = resultMD.getColumnCount();

      //Adiciona as colunas ao modelo, e define o nome das mesmas
      for(int i = 1; i <= columns; i++) {
        model.addColumn(resultMD.getColumnName(i));
      }

      //Preenche o modelo com os dados resultantes da consulta
        while(result.next()) {
            Object[] row = new Object[columns];
            for(int i = 0; i < columns; i++)
              row[i] = result.getObject(i+1);

            model.addRow(row);
        }

        result.close();
        statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return new JTable(model);
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

  private void insertData(int date, String description, double amount, String category) {
    String sql = "INSERT INTO " + tableName + "(date, description, amount, category) VALUES(?, ?, ?, ?)";

    try {
      PreparedStatement preparedST = connection.prepareStatement(sql);
      preparedST.setInt(1, date);
      preparedST.setString(2, description);
      preparedST.setDouble(3, amount);
      preparedST.setString(4, category);
      preparedST.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void deleteData(int id) {
    String sql = "DELETE FROM " + tableName + " WHERE id = ?";

    try {
      PreparedStatement preparedST = connection.prepareStatement(sql);
      preparedST.setInt(1, id);
      preparedST.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void deleteTable() {
    String sql = "DROP TABLE IF EXISTS " + tableName;

    try {
      Statement statement = connection.createStatement();
      statement.execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
