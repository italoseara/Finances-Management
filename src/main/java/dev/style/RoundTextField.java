package dev.style;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;

public class RoundTextField extends JTextField {
  private final int radius;

  private String placeholder = "";
  private Color placeholderColor = Color.GRAY;

  // Padding
  private final int left;
  private final int right;
  private final int top;
  private final int bottom;

  public RoundTextField(int radius, int left, int right, int top, int bottom) {
    this.radius = radius;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;

    setOpaque(false);
    setBorder(new RoundBorder(radius));
  }

  public RoundTextField(int radius, int vertical, int horizontal) {
    this(radius, horizontal, horizontal, vertical, vertical);
  }

  public RoundTextField(int radius) {
    this(radius, 5, 10);
  }

  public RoundTextField() {
    this(10);
  }

  public int getRadius() {
    return radius;
  }

  public void setBorderColor(Color borderColor) {
    RoundBorder border = (RoundBorder) getBorder();
    border.setColor(borderColor);
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
}
