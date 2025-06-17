package dev.chart;

import dev.style.DefaultTheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 * A modern line chart.
 */
public class LineChart extends JFreeChart {
  /**
   * Create a new line chart.
   *
   * @param color             The color of the chart.
   *
   * @param title             The title of the chart.
   * @param categoryAxisLabel The label of the category axis.
   * @param valueAxisLabel    The label of the value axis.
   * @param dataset           The dataset to use.
   */
  public LineChart(Color color, String title, String categoryAxisLabel, String valueAxisLabel,
                   CategoryDataset dataset) {
    // Temporary, because we need to call super() first
    super(new CategoryPlot());

    if (!title.isEmpty()) {
      setTitle(title);
    }

    // Create the chart
    var categoryAxis = new CategoryAxis(categoryAxisLabel);
    var valueAxis = new NumberAxis(valueAxisLabel);

    // Create the renderer
    var renderer = new LineAndShapeRenderer();
    renderer.setBaseShapesVisible(true);
    renderer.setBaseShapesFilled(true);
    renderer.setBaseLinesVisible(true);
    renderer.setBaseItemLabelsVisible(true);

    // Set the plot
    var plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
    setPlot(plot);

    // Apply the theme
    var theme = new DefaultTheme();
    theme.apply(this);

    // Set the shape of the points to a circle
    renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
    renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));
    renderer.setSeriesShape(2, new Ellipse2D.Double(-3, -3, 6, 6));
    renderer.setSeriesShape(3, new Ellipse2D.Double(-3, -3, 6, 6));

    // Styling the chart
    plot.setBackgroundPaint(null);
    plot.setOutlineVisible(false);
    plot.setDomainGridlinePaint(new Color(0xe4e4e7));
    plot.setRangeGridlinePaint(new Color(0xe4e4e7));
    plot.setDomainGridlinesVisible(true);
    plot.setRangeGridlinesVisible(true);
    plot.setDomainGridlineStroke(new BasicStroke(0.5f));
    plot.setRangeGridlineStroke(new BasicStroke(0.5f));
    plot.getDomainAxis().setAxisLineVisible(false);
    plot.getRangeAxis().setAxisLineVisible(false);
    plot.getDomainAxis().setTickMarksVisible(false);
    plot.getRangeAxis().setTickMarksVisible(false);
    renderer.setSeriesPaint(0, color);
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    setTextAntiAlias(true);
    setAntiAlias(true);
  }

  public LineChart(Color color, CategoryDataset dataset) {
    this(color, "", "", "", dataset);
  }

  public LineChart(Color color, String title, CategoryDataset dataset) {
    this(color, title, "", "", dataset);
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
