package dev.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class RoundComboBox extends JComboBox<String> {
  private final int radius;

  public RoundComboBox(String[] items, int radius, int top, int left, int bottom, int right) {
    super(items);
    this.radius = radius;
    setOpaque(false);
    setBackground(Color.WHITE);
    setForeground(Color.BLACK);
    setUI(new RoundComboBoxUI());
    setRenderer(new RoundComboBoxRenderer(5, 10, 5, 10));
    setBorder(
        new CompoundBorder(new RoundBorder(radius), new EmptyBorder(top, left, bottom, right)));

  }

  public RoundComboBox(String[] items, int radius, int vertical, int horizontal) {
    this(items, radius, horizontal, horizontal, vertical, vertical);
  }

  public RoundComboBox(String[] items, int radius) {
    this(items, radius, 5, 10);
  }

  public RoundComboBox(int radius) {
    this(new String[0], radius);
  }

  public int getRadius() {
    return radius;
  }

  public void setBorderColor(Color color) {
    CompoundBorder border = (CompoundBorder) getBorder();
    ((RoundBorder) border.getOutsideBorder()).setColor(color);
  }

  public void setSelectionColor(Color color) {
    UIManager.put("ComboBox.selectionBackground", color);
  }

  @Override
  public void setBackground(Color bg) {
    super.setBackground(bg);
    UIManager.put("ComboBox.background", bg);
    UIManager.put("ComboBox.selectionBackground", new Color(0xf8f4f4));
  }

  @Override
  public void setForeground(Color fg) {
    super.setForeground(fg);
    UIManager.put("ComboBox.foreground", fg);
    UIManager.put("ComboBox.selectionForeground", fg);
  }

  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(getBackground());
    g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    super.paintComponent(g);
  }

  protected static class RoundComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
      return new JButton() {
        @Override
        public int getWidth() {
          return 0;
        }
      };
    }

    @Override
    protected ComboPopup createPopup() {
      BasicComboPopup popup = new BasicComboPopup(comboBox);
      popup.setBorder(new EmptyBorder(0, 0, 0, 0));
      return popup;
    }

    @Override
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
      // Do not paint the background
      // super.paintCurrentValueBackground(g, bounds, hasFocus);

      // Paint the selected item
      ListCellRenderer<Object> renderer = comboBox.getRenderer();
      Component c =
          renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false,
              false);
      c.setFont(comboBox.getFont());
      if (hasFocus && !isPopupVisible(comboBox)) {
        c.setForeground(listBox.getForeground());
        c.setBackground(listBox.getBackground());
      } else {
        c.setForeground(comboBox.getForeground());
        c.setBackground(comboBox.getBackground());
      }
      boolean shouldValidate = c instanceof JPanel;

      int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
      if (padding != null) {
        x = bounds.x + padding.left;
        y = bounds.y + padding.top;
        w = bounds.width - (padding.left + padding.right);
        h = bounds.height - (padding.top + padding.bottom);
      }

      currentValuePane.paintComponent(g, c, comboBox, x, y, w, h, shouldValidate);

      // draw fake arrow on the right
      int arrowWidth = 10;
      int arrowHeight = 5;
      int arrowX = bounds.x + bounds.width - arrowWidth - 5;
      int arrowY = bounds.y + (bounds.height - arrowHeight) / 2;
      g.setColor(comboBox.getForeground());
      g.drawLine(arrowX, arrowY, arrowX + arrowWidth / 2, arrowY + arrowHeight);
      g.drawLine(arrowX + arrowWidth / 2, arrowY + arrowHeight, arrowX + arrowWidth, arrowY);
    }
  }

  protected static class RoundComboBoxRenderer extends DefaultListCellRenderer {
    private final int top;
    private final int left;
    private final int bottom;
    private final int right;

    public RoundComboBoxRenderer(int top, int left, int bottom, int right) {
      this.top = top;
      this.left = left;
      this.bottom = bottom;
      this.right = right;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      if (index != -1) {
        setBorder(new EmptyBorder(top, left, bottom, right));
      }
      return this;
    }
  }
}
