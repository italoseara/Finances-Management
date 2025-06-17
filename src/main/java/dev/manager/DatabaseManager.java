package dev.manager;

import dev.style.DBTable;
import dev.util.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

public class DatabaseManager {
    private static Connection connection;

    public static void connect(String filename) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            return;
        }

        if (connection == null) {
            Utilities.showErrorMessage("Failed to connect to the database.");
            return;
        }

        if (!createTables()) {
            Utilities.showErrorMessage("Failed to create tables.");
            return;
        }

        populateWithFakeData();
    }

    private static boolean createTables() {
        try {
            Statement statement = connection.createStatement();

            // Tabela de clientes
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS customers (
                        id     INTEGER PRIMARY KEY AUTOINCREMENT,
                        name   TEXT NOT NULL,
                        email  TEXT,
                        phone  TEXT
                    );
                    """);

            // Tabela de vendas
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS sales (
                        id          INTEGER PRIMARY KEY AUTOINCREMENT,
                        date        TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        customer_id INTEGER,
                        description TEXT NOT NULL,
                        amount      REAL NOT NULL,
                        FOREIGN KEY (customer_id) REFERENCES customers(id)
                    );
                    """);

            // Tabela de despesas
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS expenses (
                        id          INTEGER PRIMARY KEY AUTOINCREMENT,
                        date        TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        category    TEXT NOT NULL,
                        description TEXT NOT NULL,
                        amount      REAL NOT NULL
                    );
                    """);

            statement.close();
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            return false;
        }

        return true;
    }

    public static void populateWithFakeData() {
        try {
            Statement statement = connection.createStatement();

            // Verifica se já existem dados
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM sales");
            result.next();
            if (result.getInt(1) > 0) {
                return;
            }

            // Clientes fictícios
            String[] names = {"Maria", "João", "Carlos", "Ana", "Fernanda"};
            for (String name : names) {
                String phone = "(%s) %04d-%04d".formatted(
                        String.format("%02d", new Random().nextInt(99)),
                        new Random().nextInt(10000),
                        new Random().nextInt(10000));
                statement.execute("""
                            INSERT INTO customers (name, email, phone)
                            VALUES ('%s', '%s@email.com', '%s');
                        """.formatted(name, name.toLowerCase(), phone));
            }

            // Vendas fictícias
            Random random = new Random();
            for (int i = 1; i <= 50; i++) {
                int customerId = random.nextInt(names.length) + 1;
                double amount = 100 + random.nextDouble() * 900;
                int month = random.nextInt(6) + 1;
                int day = random.nextInt(28) + 1;
                String date = "2024-%02d-%02d".formatted(month, day);

                statement.execute("""
                          INSERT INTO sales (date, customer_id, description, amount)
                          VALUES ('%s', %d, 'Venda #%d', %.2f);
                        """.formatted(date, customerId, i, amount));
            }

            // Despesas fictícias
            String[] categories = {"Aluguel", "Internet", "Energia", "Transporte"};
            for (int i = 1; i <= 30; i++) {
                String category = categories[random.nextInt(categories.length)];
                double amount = 50 + random.nextDouble() * 500;
                int month = random.nextInt(6) + 1;
                int day = random.nextInt(28) + 1;
                String date = "2024-%02d-%02d".formatted(month, day);

                statement.execute("""
                          INSERT INTO expenses (date, category, description, amount)
                          VALUES ('%s', '%s', 'Despesa #%d', %.2f);
                        """.formatted(date, category, i, amount));
            }

            statement.close();
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
    }

    public static ResultSet query(String query, Object... params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void update(String query, Object... params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
    }

    public static Integer queryAsInt(String query, Object... params) {
        try {
            ResultSet result = query(query, params);
            if (result != null && result.next()) {
                return result.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            return null;
        }
    }

    public static double queryAsDouble(String query, Object... params) {
        try {
            ResultSet result = query(query, params);
            if (result != null && result.next()) {
                return result.getDouble(1);
            }
            return -1;
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            return -1;
        }
    }

    public static String[] queryAsArray(String query) {
        try {
            String countQuery = "SELECT COUNT(*) FROM (" + query.replace(";", "") + ");";
            Statement statement = connection.createStatement();
            ResultSet countResult = statement.executeQuery(countQuery);

            countResult.next();
            int count = countResult.getInt(1);

            String[] arr = new String[count];
            ResultSet result = statement.executeQuery(query);
            for (int i = 0; result.next(); i++) {
                arr[i] = result.getString(1);
            }

            countResult.close();
            result.close();
            statement.close();

            return arr;
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            return null;
        }
    }

    public static DBTable queryAsTable(String query) {
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
            return new DBTable(model, query);
        } catch (SQLException e) {
            Utilities.showErrorMessage(e.getMessage());
            return null;
        }
    }
}