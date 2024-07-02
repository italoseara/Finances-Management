package dev.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

public class RoundBorder extends AbstractBorder {
  private final int radius;
  private Color borderColor = Color.BLACK;

  public RoundBorder(int radius) {
    this.radius = radius;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(borderColor);
    g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

    g2.dispose();
  }

  public void setColor(Color borderColor) {
    this.borderColor = borderColor;
  }
}
