package dev.components;

import dev.chart.BarChart;
import dev.chart.LineChart;
import dev.chart.PieChart;
import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.RoundBorder;
import dev.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class Dashboard extends JPanel {
  private static Dashboard instance;

  private final JPanel totalIncome;
  private final ChartPanel totalIncomeChart;

  private final JPanel totalExpenses;
  private final ChartPanel totalExpensesChart;

  private final JPanel expansesByCategory;
  private final ChartPanel expansesByCategoryChart;

  public Dashboard() {
    setBackground(Color.WHITE);
    setLayout(null);

    totalIncome = createCard("Total Income (6 months)", getIncome());
    totalIncomeChart = createTotalIncomeChart();
    totalIncome.add(totalIncomeChart);
    add(totalIncome);

    totalExpenses = createCard("Total Expenses (6 months)", getExpenses());
    totalExpensesChart = createTotalExpansesChart();
    totalExpenses.add(totalExpensesChart);
    add(totalExpenses);

    expansesByCategory = createCard("Expenses by Category", "Highest: " + getHighestExpenseCategory());
    expansesByCategoryChart = createExpansesByCategoriesChart();
    expansesByCategory.add(expansesByCategoryChart);
    add(expansesByCategory);

    instance = this;
  }

  @SuppressWarnings("deprecation")
  private ChartPanel createTotalIncomeChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    // Get data for the last 6 months (including the current month)
    int month = new Date().getMonth() + 1 - 5;
    for (int i = 0; i < 6; i++) {
      String monthName = Utilities.getMonthName(month);
      double income = DatabaseManager.queryAsDouble(
          "SELECT SUM(amount) FROM transactions WHERE amount > 0 AND strftime('%%m', date) = '%02d';".formatted(
              month));
      dataset.addValue(income, "Income", monthName);

      month++;
      if (month > 12) {
        month = 1;
      }
    }

    LineChart chart = new LineChart(new Color(0x2563EB), dataset);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDomainZoomable(false);
    chartPanel.setRangeZoomable(false);
    chartPanel.setPopupMenu(null);
    chartPanel.setLocation(20, 100);

    return chartPanel;
  }

  @SuppressWarnings("deprecation")
  private ChartPanel createTotalExpansesChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    // Get data for the last 6 months (including the current month)
    int month = new Date().getMonth() + 1 - 5;
    for (int i = 0; i < 6; i++) {
      String monthName = Utilities.getMonthName(month);
      double expenses = -DatabaseManager.queryAsDouble(
          "SELECT SUM(amount) FROM transactions WHERE amount < 0 AND strftime('%%m', date) = '%02d';".formatted(
              month));
      dataset.addValue(expenses, "Expenses", monthName);

      month++;
      if (month > 12) {
        month = 1;
      }
    }

    BarChart chart = new BarChart(new Color(0xDC0D3A), dataset);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDomainZoomable(false);
    chartPanel.setRangeZoomable(false);
    chartPanel.setPopupMenu(null);
    chartPanel.setLocation(20, 100);

    return chartPanel;
  }

  private ChartPanel createExpansesByCategoriesChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    // Get all categories names and id
    var categoriesResultSet = DatabaseManager.query("SELECT id, name FROM categories;");
    assert categoriesResultSet != null;
    try {
      while (categoriesResultSet.next()) {
        int categoryId = categoriesResultSet.getInt("id");
        String categoryName = categoriesResultSet.getString("name");

        // Get the sum of expenses for each category
        double expenses = -DatabaseManager.queryAsDouble(
            "SELECT SUM(amount) FROM transactions WHERE category_id = %d AND amount < 0;".formatted(
                categoryId));

        if (expenses != 0) {
          dataset.addValue(expenses, "Expenses", categoryName);
        }
      }
    } catch (SQLException e) {
      Utilities.showErrorMessage("An error occurred while fetching the categories.");
    }

    PieChart chart = new PieChart(new Color[] {
        new Color(0x2563EB),
        new Color(0x6D214F),
        new Color(0xF18701),
        new Color(0x00A3AD),
        new Color(0x8C8C8C),
        new Color(0x1A1A1A),
    }, dataset);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPopupMenu(null);
    chartPanel.setLocation(20, 100);

    return chartPanel;
  }

  private JPanel createCard(String title, String amount) {
    JPanel card = new JPanel();
    card.setLayout(null);
    card.setBackground(Color.WHITE);
    card.setBorder(new RoundBorder(new Color(0xe4e4e7), 20));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setBounds(25, 20, 200, 30);
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

  private String getIncome() {
    double income =
        DatabaseManager.queryAsDouble("SELECT SUM(amount) FROM transactions WHERE amount > 0;");
    return Utilities.formatCurrency(income);
  }

  private String getExpenses() {
    double expenses =
        -DatabaseManager.queryAsDouble("SELECT SUM(amount) FROM transactions WHERE amount < 0;");
    return Utilities.formatCurrency(expenses);
  }

  private String getHighestExpenseCategory() {
    var resultSet = DatabaseManager.query(
        "SELECT name FROM categories WHERE id = (SELECT category_id FROM transactions WHERE amount = (SELECT MIN(amount) FROM transactions WHERE amount < 0));");
    assert resultSet != null;
    try {
      return resultSet.getString("name");
    } catch (SQLException e) {
      return "N/A";
    }
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);

    // there will be 3 cards in a row with 20px margin between them and 20px from the wall
    totalIncome.setBounds(20, 20, (width - 80) / 3, 320);
    totalIncomeChart.setBounds(20, 100, (width - 80) / 3 - 40, 200);

    totalExpenses.setBounds(40 + (width - 80) / 3, 20, (width - 80) / 3, 320);
    totalExpensesChart.setBounds(20, 100, (width - 80) / 3 - 40, 200);

    expansesByCategory.setBounds(60 + 2 * (width - 80) / 3, 20, (width - 80) / 3, 320);
    expansesByCategoryChart.setBounds(1, 90, (width) / 3 - 30, 210);
  }

  public void refresh() {
    JLabel totalIncomeLabel = (JLabel) totalIncome.getComponent(1);
    JLabel totalExpensesLabel = (JLabel) totalExpenses.getComponent(1);

    totalIncomeChart.setChart(createTotalIncomeChart().getChart());
    totalIncomeLabel.setText(getIncome());

    totalExpensesChart.setChart(createTotalExpansesChart().getChart());
    totalExpensesLabel.setText(getExpenses());
  }

  public static Dashboard getInstance() {
    return instance;
  }
}
