package dev.chart;

import dev.style.DefaultTheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.lang.reflect.Field;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.TextAnchor;

/**
 * A modern bar chart.
 */
public class BarChart extends JFreeChart {
  /**
   * Create a new bar chart.
   *
   * @param color             The color of the chart.
   * @param title             The title of the chart.
   * @param categoryAxisLabel The label of the category axis.
   * @param valueAxisLabel    The label of the value axis.
   * @param dataset           The dataset to use.
   * @param orientation       The orientation of the chart.
   */
  public BarChart(Color color, String title, String categoryAxisLabel, String valueAxisLabel,
                  CategoryDataset dataset, PlotOrientation orientation) {
    // Temporary, because we need to call super() first
    // Edit: Not temporary at all XD
    super(new CategoryPlot());

    if (!title.isEmpty()) {
      setTitle(title);
    }

    // Create the chart
    var categoryAxis = new CategoryAxis(categoryAxisLabel);
    var valueAxis = new NumberAxis(valueAxisLabel);

    // Create the renderer
    var renderer = new BarRenderer();
    var positive = orientation == PlotOrientation.HORIZONTAL ?
        new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT) :
        new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER);
    var negative = orientation == PlotOrientation.HORIZONTAL ?
        new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT) :
        new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_CENTER);
    renderer.setBasePositiveItemLabelPosition(positive);
    renderer.setBaseNegativeItemLabelPosition(negative);

    // Set the plot
    var plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
    plot.setOrientation(orientation);
    setPlot(plot);

    // Apply the theme
    var theme = new DefaultTheme();
    theme.apply(this);

    // Styling the chart
    renderer.setMaximumBarWidth(0.1);
    renderer.setSeriesPaint(0, color);
    renderer.setShadowVisible(false);
    plot.setOutlineVisible(false);
    plot.getRangeAxis().setAxisLineVisible(false);
    plot.getRangeAxis().setTickMarksVisible(false);
    plot.setRangeGridlineStroke(new BasicStroke());
    plot.getRangeAxis().setTickLabelPaint(new Color(51, 51, 51));
    plot.getDomainAxis().setTickLabelPaint(new Color(51, 51, 51));
    setTextAntiAlias(true);
    setAntiAlias(true);
  }

  public BarChart(Color color, String title, String categoryAxisLabel, String valueAxisLabel,
                  CategoryDataset dataset) {
    this(color, title, categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.VERTICAL);
  }

  public BarChart(Color color, String title, CategoryDataset dataset, PlotOrientation orientation) {
    this(color, title, "", "", dataset, orientation);
  }

  public BarChart(Color color, String title, CategoryDataset dataset) {
    this(color, title, "", "", dataset, PlotOrientation.VERTICAL);
  }

  public BarChart(Color color, String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset,
                  PlotOrientation orientation) {
    this(color, "", categoryAxisLabel, valueAxisLabel, dataset, orientation);
  }

  public BarChart(Color color, String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset) {
    this(color, "", categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.VERTICAL);
  }

  public BarChart(Color color, CategoryDataset dataset, PlotOrientation orientation) {
    this(color, "", "", "", dataset, orientation);
  }

  public BarChart(Color color, CategoryDataset dataset) {
    this(color, "", "", "", dataset, PlotOrientation.VERTICAL);
  }

  /**
   * Set the plot of the chart.
   *
   * @param plot The plot to set.
   */
  private void setPlot(CategoryPlot plot) {
    try {
      Field field = JFreeChart.class.getDeclaredField("plot");
      field.setAccessible(true);
      field.set(this, plot);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
