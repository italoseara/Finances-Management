package dev.chart;

import dev.manager.FontManager;
import dev.style.DefaultTheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.lang.reflect.Field;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.TableOrder;

/**
 * A modern pie chart.
 */
@SuppressWarnings("deprecation")
public class PieChart extends JFreeChart {
  /**
   * Create a new pie chart.
   */
  public PieChart(Color[] colors, CategoryDataset dataset) {
    super(new PiePlot());

    // Convert CategoryDataset to PieDataset
    PieDataset pieDataset = new CategoryToPieDataset(dataset, TableOrder.BY_ROW, 0);

    // Create the plot
    var plot = new PiePlot(pieDataset);
    setBorderVisible(false);
    plot.setOutlineVisible(false);
    plot.setSimpleLabels(true);
    plot.setLabelBackgroundPaint(null);
    plot.setLabelOutlinePaint(null);
    plot.setLabelShadowPaint(null);
    plot.setCircular(true);
    plot.setNoDataMessage("No data available");
    plot.setInteriorGap(0);
    plot.setBaseSectionOutlinePaint(Color.WHITE);
    plot.setBaseSectionPaint(Color.WHITE);
    plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));
    plot.setLabelFont(FontManager.getFont("Inter", Font.PLAIN, 14)
        .deriveFont(Map.of(TextAttribute.TRACKING, 0.02)));
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));
    plot.setShadowPaint(null);

    // Set the colors of the pie chart
    for (int i = 0; i < colors.length; i++) {
      plot.setSectionPaint(i, colors[i]);
    }

    // Set the plot
    setPlot(plot);

    // Apply the theme
    var theme = new DefaultTheme();
    theme.apply(this);
  }

  /**
   * Set the plot of the chart.
   *
   * @param plot The plot to set.
   */
  private void setPlot(PiePlot plot) {
    try {
      Field field = JFreeChart.class.getDeclaredField("plot");
      field.setAccessible(true);
      field.set(this, plot);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}