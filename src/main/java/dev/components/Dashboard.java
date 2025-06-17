package dev.components;

import dev.chart.BarChart;
import dev.chart.LineChart;
import dev.chart.PieChart;
import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.style.RoundBorder;
import dev.util.Utilities;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.swing.*;

import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class Dashboard extends JPanel {
  private static Dashboard instance;

  private final JPanel totalSales;
  private final ChartPanel totalSalesChart;

  private final JPanel totalExpenses;
  private final ChartPanel totalExpensesChart;

  private final JPanel expensesByCategory;
  private final ChartPanel expensesByCategoryChart;

  private final ModernScrollPane latestTransactions;
  private final JLabel infoLabel;

  public Dashboard() {
    setBackground(Color.WHITE);
    setLayout(null);

    totalSales = createCard("Total de Vendas", getTotalSales());
    totalSalesChart = createSalesChart();
    totalSales.add(totalSalesChart);
    add(totalSales);

    totalExpenses = createCard("Total de Despesas", getTotalExpenses());
    totalExpensesChart = createExpensesChart();
    totalExpenses.add(totalExpensesChart);
    add(totalExpenses);

    expensesByCategory = createCard("Despesas por Categoria", "");
    expensesByCategoryChart = createExpensesByCategoryChart();
    expensesByCategory.add(expensesByCategoryChart);
    add(expensesByCategory);

    JLabel latestTransactionsLabel = new JLabel("Últimas Transações");
    latestTransactionsLabel.setFont(FontManager.getFont("Inter", Font.BOLD, 24)
            .deriveFont(Map.of(TextAttribute.TRACKING, 0.04)));
    latestTransactionsLabel.setBounds(20, 360, 500, 30);
    latestTransactionsLabel.setForeground(new Color(0x111827));
    add(latestTransactionsLabel);

    infoLabel = new JLabel("Maior despesa: %s | Maior venda: %s".formatted(getHighestExpense(), getHighestSale()));
    infoLabel.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    infoLabel.setBounds(20, 385, 500, 30);
    infoLabel.setForeground(new Color(0x6B7280));
    add(infoLabel);

    latestTransactions = createLatestTransactions();
    add(latestTransactions);

    instance = this;
  }

  private ModernScrollPane createLatestTransactions() {
    DBTable table = DatabaseManager.queryAsTable("""
        SELECT 'Venda' as tipo, date, description, amount FROM sales
        UNION ALL
        SELECT 'Despesa' as tipo, date, description, amount FROM expenses
        ORDER BY date DESC LIMIT 9;
        """);

    ModernScrollPane scrollPane = new ModernScrollPane(table);
    scrollPane.setHeader(new String[] {"Tipo", "Data", "Descrição", "Valor"});
    scrollPane.setColumnsWidth(new double[] {.15, .2, .45, .2});
    scrollPane.setColumnsFormat((Object[] row) -> {
      if (row[1] instanceof String date) row[1] = Utilities.formatDate(date);
      double amount = Utilities.parseDouble(row[3].toString());
      row[3] = Utilities.formatCurrency(amount);
      row[3] = "<html><font color='%s'>%s</font></html>".formatted(
              row[0].equals("Despesa") ? "#FF0000" : "#008000", row[3]);
      return row;
    });

    return scrollPane;
  }

  @SuppressWarnings("deprecation")
  private ChartPanel createSalesChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int month = new Date().getMonth() + 1 - 5;
    for (int i = 0; i < 6; i++) {
      String monthName = Utilities.getMonthName(month);
      double value = DatabaseManager.queryAsDouble(
              "SELECT SUM(amount) FROM sales WHERE strftime('%%m', date) = '%02d';".formatted(month));
      dataset.addValue(value, "Vendas", monthName);
      if (++month > 12) month = 1;
    }
    LineChart chart = new LineChart(new Color(0x2563EB), dataset);
    ChartPanel panel = new ChartPanel(chart);
    panel.setDomainZoomable(false);
    panel.setRangeZoomable(false);
    panel.setPopupMenu(null);
    return panel;
  }

  @SuppressWarnings("deprecation")
  private ChartPanel createExpensesChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int month = new Date().getMonth() + 1 - 5;
    for (int i = 0; i < 6; i++) {
      String monthName = Utilities.getMonthName(month);
      double value = DatabaseManager.queryAsDouble(
              "SELECT SUM(amount) FROM expenses WHERE strftime('%%m', date) = '%02d';".formatted(month));
      dataset.addValue(value, "Despesas", monthName);
      if (++month > 12) month = 1;
    }
    BarChart chart = new BarChart(new Color(0xDC0D3A), dataset);
    ChartPanel panel = new ChartPanel(chart);
    panel.setDomainZoomable(false);
    panel.setRangeZoomable(false);
    panel.setPopupMenu(null);
    return panel;
  }

  private ChartPanel createExpensesByCategoryChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    var result = DatabaseManager.query("""
        SELECT category, SUM(amount) as total FROM expenses GROUP BY category;
        """);
    try {
      while (result.next()) {
        String category = result.getString("category");
        double total = result.getDouble("total");
        if (total != 0) dataset.addValue(total, "Despesas", category);
      }
    } catch (SQLException e) {
      Utilities.showErrorMessage("Erro ao buscar categorias.");
    }
    PieChart chart = new PieChart(
            new Color[] {new Color(0x2563EB), new Color(0x6D214F), new Color(0xF18701),
                    new Color(0x00A3AD), new Color(0x8C8C8C), new Color(0x1A1A1A)}, dataset);
    ChartPanel panel = new ChartPanel(chart);
    panel.setPopupMenu(null);
    return panel;
  }

  private JPanel createCard(String title, String amount) {
    JPanel card = new JPanel();
    card.setLayout(null);
    card.setBackground(Color.WHITE);
    card.setBorder(new RoundBorder(new Color(0xe4e4e7), 20));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setBounds(25, 20, 250, 30);
    titleLabel.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    titleLabel.setForeground(new Color(0x71717a));
    card.add(titleLabel);

    JLabel amountLabel = new JLabel(amount);
    amountLabel.setBounds(25, 50, 250, 30);
    amountLabel.setFont(FontManager.getFont("Inter", Font.PLAIN, 22)
            .deriveFont(Map.of(TextAttribute.TRACKING, 0.02)));
    amountLabel.setForeground(new Color(0x1a1a1a));
    card.add(amountLabel);

    return card;
  }

  private String getTotalSales() {
    return Utilities.formatCurrency(DatabaseManager.queryAsDouble("SELECT SUM(amount) FROM sales;"));
  }

  private String getTotalExpenses() {
    return Utilities.formatCurrency(DatabaseManager.queryAsDouble("SELECT SUM(amount) FROM expenses;"));
  }

  private String getHighestExpense() {
    return Utilities.formatCurrency(DatabaseManager.queryAsDouble("SELECT MAX(amount) FROM expenses;"));
  }

  private String getHighestSale() {
    return Utilities.formatCurrency(DatabaseManager.queryAsDouble("SELECT MAX(amount) FROM sales;"));
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);

    // there will be 3 cards in a row with 20px margin between them and 20px from the wall
    totalSales.setBounds(20, 20, (width - 80) / 3, 320);
    totalSalesChart.setBounds(20, 100, (width - 80) / 3 - 40, 200);

    totalExpenses.setBounds(40 + (width - 80) / 3, 20, (width - 80) / 3, 320);
    totalExpensesChart.setBounds(20, 100, (width - 80) / 3 - 40, 200);

    expensesByCategory.setBounds(60 + 2 * (width - 80) / 3, 20, (width - 80) / 3, 320);
    expensesByCategoryChart.setBounds(1, 90, (width) / 3 - 30, 210);

    latestTransactions.setBounds(20, 420, width - 40, height - 390);
  }

  public void refresh() {
    ((JLabel) totalSales.getComponent(1)).setText(getTotalSales());
    totalSalesChart.setChart(createSalesChart().getChart());

    ((JLabel) totalExpenses.getComponent(1)).setText(getTotalExpenses());
    totalExpensesChart.setChart(createExpensesChart().getChart());

    expensesByCategoryChart.setChart(createExpensesByCategoryChart().getChart());

    infoLabel.setText("Maior despesa: %s | Maior venda: %s".formatted(getHighestExpense(), getHighestSale()));
  }

  public static Dashboard getInstance() {
    return instance;
  }
}