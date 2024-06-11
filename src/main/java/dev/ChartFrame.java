package dev;

import dev.chart.BarChart;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartFrame extends JFrame {
  public ChartFrame() {
    setTitle("Bar Chart Example");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(null);
    getContentPane().setBackground(Color.WHITE);

    // Create the chart
    JFreeChart chart = createChart();
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDomainZoomable(false);
    chartPanel.setRangeZoomable(false);
    chartPanel.setPopupMenu(null);
    chartPanel.setSize(new Dimension(350, 300));

    // Add the chart to the frame
    add(chartPanel);
    setVisible(true);
  }

  private CategoryDataset createDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(111, "Expense", "Jan");
    dataset.addValue(157, "Expense", "Feb");
    dataset.addValue(129, "Expense", "Mar");
    dataset.addValue(150, "Expense", "Apr");
    dataset.addValue(119, "Expense", "May");
    dataset.addValue(72, "Expense", "Jun");
    return dataset;
  }

  private JFreeChart createChart() {
    return new BarChart(createDataset());
  }
}
  