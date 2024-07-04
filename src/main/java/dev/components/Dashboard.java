package dev.components;

import dev.chart.BarChart;
import dev.chart.LineChart;
import dev.manager.FontManager;
import dev.style.RoundBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class Dashboard extends JPanel {
  private final JPanel totalIncome;
  private final ChartPanel totalIncomeChart;

  private final JPanel totalExpenses;
  private final ChartPanel totalExpensesChart;

  public Dashboard() {
    setBackground(Color.WHITE);
    setLayout(null);

    totalIncome = createCard("Total Income", "R$ 2.000,00");
    totalIncomeChart = createTotalIncomeChart();
    totalIncome.add(totalIncomeChart);
    add(totalIncome);

    totalExpenses = createCard("Total Expenses", "R$ 1.000,00");
    totalExpensesChart = createTotalExpansesChart();
    totalExpenses.add(totalExpensesChart);
    add(totalExpenses);
  }

  private ChartPanel createTotalIncomeChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(43, "Income", "Jan");
    dataset.addValue(137, "Income", "Feb");
    dataset.addValue(61, "Income", "Mar");
    dataset.addValue(145, "Income", "Apr");
    dataset.addValue(26, "Income", "May");
    dataset.addValue(154, "Income", "Jun");

    LineChart chart = new LineChart(new Color(0x2563EB), dataset);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDomainZoomable(false);
    chartPanel.setRangeZoomable(false);
    chartPanel.setPopupMenu(null);
    chartPanel.setLocation(20, 100);

    return chartPanel;
  }

  private ChartPanel createTotalExpansesChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(111, "Expense", "Jan");
    dataset.addValue(157, "Expense", "Feb");
    dataset.addValue(129, "Expense", "Mar");
    dataset.addValue(150, "Expense", "Apr");
    dataset.addValue(119, "Expense", "May");
    dataset.addValue(72, "Expense", "Jun");

    BarChart chart = new BarChart(new Color(0xDC0D3A), dataset);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDomainZoomable(false);
    chartPanel.setRangeZoomable(false);
    chartPanel.setPopupMenu(null);
    chartPanel.setLocation(20, 100);

    return chartPanel;
  }

  private JPanel createCard(String title, String amount) {
    JPanel totalExpenses = new JPanel();
    totalExpenses.setLayout(null);
    totalExpenses.setBackground(Color.WHITE);
    totalExpenses.setBorder(new RoundBorder(new Color(0xe4e4e7), 20));

    JLabel totalExpensesTitle = new JLabel(title);
    totalExpensesTitle.setBounds(25, 20, 200, 30);
    totalExpensesTitle.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    totalExpensesTitle.setForeground(new Color(0x71717a));
    totalExpenses.add(totalExpensesTitle);

    JLabel totalExpensesAmount = new JLabel(amount);
    totalExpensesAmount.setBounds(25, 50, 200, 30);
    totalExpensesAmount.setFont(FontManager.getFont("Inter", Font.PLAIN, 22)
        .deriveFont(Map.of(TextAttribute.TRACKING, 0.02)));
    totalExpensesAmount.setForeground(new Color(0x1a1a1a));
    totalExpenses.add(totalExpensesAmount);

    return totalExpenses;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);

    // there will be 3 cards in a row with 20px margin between them and 20px from the wall
    totalIncome.setBounds(20, 20, (width - 80) / 3, 320);
    totalIncomeChart.setBounds(20, 100, (width - 80) / 3 - 40, 200);

    totalExpenses.setBounds(40 + (width - 80) / 3, 20, (width - 80) / 3, 320);
    totalExpensesChart.setBounds(20, 100, (width - 80) / 3 - 40, 200);
  }
}
