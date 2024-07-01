package dev.style;

import dev.manager.FontManager;
import java.awt.Color;
import java.awt.Font;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.ui.RectangleInsets;

/**
 * Modern theme for JFreeChart.
 */
public class DefaultTheme extends StandardChartTheme {
  public DefaultTheme() {
    super("JFree");

    // Font
    String fontName = "font";
    setExtraLargeFont(new Font(fontName, Font.PLAIN, 16));
    setLargeFont(new Font(fontName, Font.BOLD, 15));
    setRegularFont(new Font(fontName, Font.PLAIN, 12));
    setSmallFont(new Font(fontName, Font.PLAIN, 10));

    // Colors
    setTitlePaint(Color.BLACK);
    setRangeGridlinePaint(new Color(240, 240, 240));
    setAxisLabelPaint(new Color(102, 102, 102));
    setPlotBackgroundPaint(Color.WHITE);
    setChartBackgroundPaint(Color.WHITE);
    setGridBandPaint(Color.RED);

    // Padding
    setAxisOffset(new RectangleInsets(0, 0, 0, 0));

    // Bar chart
    setBarPainter(new StandardBarPainter());
  }
}
