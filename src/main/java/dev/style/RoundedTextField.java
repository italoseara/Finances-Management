package dev.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;

public class RoundedTextField extends JTextField {
  private final int radius;
  private Color borderColor = Color.BLACK;

  private String placeholder = "";
  private Color placeholderColor = Color.GRAY;

  // Padding
  private final int left;
  private final int right;
  private final int top;
  private final int bottom;

  public RoundedTextField(int radius, int left, int right, int top, int bottom) {
    this.radius = radius;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;

    setOpaque(false);
    setBorder(new RoundedBorder(radius));
  }

  public RoundedTextField(int radius, int vertical, int horizontal) {
    this(radius, horizontal, horizontal, vertical, vertical);
  }

  public RoundedTextField(int radius) {
    this(radius, 5, 10);
  }

  public RoundedTextField() {
    this(10);
  }

  public int getRadius() {
    return radius;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public Color getPlaceholderColor() {
    return placeholderColor;
  }

  public void setPlaceholderColor(Color placeholderColor) {
    this.placeholderColor = placeholderColor;
  }

  @Override
  public Insets getInsets() {
    return new Insets(top, left, bottom, right);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

    // Draw the placeholder
    if (getText().isEmpty() && !placeholder.isEmpty()) {
      g2.setColor(getPlaceholderColor());
      g2.drawString(placeholder, getInsets().left,
          g2.getFontMetrics().getMaxAscent() + getInsets().top);
    }

    super.paintComponent(g2);
    g2.dispose();
  }

  private static class RoundedBorder extends AbstractBorder {
    private final int radius;

    public RoundedBorder(int radius) {
      this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      RoundedTextField textField = (RoundedTextField) c;
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      g2.setColor(textField.getBorderColor());
      g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

      g2.dispose();
    }
  }
}
