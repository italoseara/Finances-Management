package dev.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.Border;

public class RoundedButton extends JButton {
  private final int radius;
  private final int top;
  private final int left;
  private final int bottom;
  private final int right;

  private boolean isHovered = false;

  private Color hoverColor;

  public RoundedButton(String text, int radius, int top, int left, int bottom, int right) {
    super(text);
    this.radius = radius;
    this.top = top;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
    setFocusPainted(false);
    setContentAreaFilled(false);
    addMouseListener(new MouseListener());
    setBorder(new RoundedBorder(radius));
  }

  public RoundedButton(String text, int radius, int horizontal, int vertical) {
    this(text, radius, vertical, horizontal, vertical, horizontal);
  }

  public RoundedButton(String text, int radius) {
    this(text, radius, 0, 0);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (!isHovered) {
      g2.setPaint(getBackground());
    } else {
      g2.setPaint(hoverColor);
    }

    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

    super.paintComponent(g2);
    g2.dispose();
  }

  @Override
  public Insets getInsets() {
    return new Insets(top, left, bottom, right);
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.setSize(size.width + radius, size.height + radius);
    return size;
  }

  public Color getHoverColor() {
    return hoverColor;
  }

  public void setHoverColor(Color hoverColor) {
    this.hoverColor = hoverColor;
  }

  public void setBorderColor(Color borderColor) {
    ((RoundedBorder) getBorder()).setColor(borderColor);
  }

  private static class MouseListener extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
      RoundedButton button = (RoundedButton) e.getSource();
      button.isHovered = true;
      button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
      RoundedButton button = (RoundedButton) e.getSource();
      button.isHovered = false;
      button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
  }

  private static class RoundedBorder implements Border {
    private final int radius;
    private Color color = Color.BLACK;

    RoundedBorder(int radius) {
      this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
      return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      g.setColor(color);
      g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    public boolean isBorderOpaque() {
      return true;
    }

    public void setColor(Color color) {
      this.color = color;
    }
  }
}
