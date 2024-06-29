package dev.components;

import dev.App;
import dev.manager.FontManager;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NavButton extends JButton {
  private final int radius = 15;

  private final Color textColor = new Color(0x6b7280);
  private final Color hoverTextColor = new Color(0x111827);

  private final Color activeColor = new Color(0x111827);
  private final Color activeBackgroundColor = new Color(0xf3f4f6);

  private boolean isActive = false;
  private boolean isHovered = false;

  private final JPanel panel;
  private final NavBar parent;

  public NavButton(NavBar parent, String iconPath, String text, JPanel panel) {
    super(text);
    this.panel = panel;
    this.parent = parent;

    setIcon(getIcon(iconPath));
    setIconTextGap(10);
    setHorizontalAlignment(JButton.LEFT);
    setPreferredSize(new Dimension(260, 23));
    setContentAreaFilled(false);
    addMouseListener(new MouseListener());
    setBackground(new Color(0, 0, 0, 0));
    setForeground(textColor);
    setFocusPainted(false);
    setBorderPainted(false);
    setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
  }

  private ImageIcon getIcon(String path) {
    Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource(path))).getImage();
    return new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING));
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
    setBackground(isActive ? activeBackgroundColor : new Color(0, 0, 0, 0));
    setForeground(isActive ? activeColor : isHovered ? hoverTextColor : textColor);
    App.getInstance().setContent(panel);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setPaint(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

    super.paintComponent(g2);
    g2.dispose();
  }

  @Override
  public Insets getInsets() {
    return new Insets(0, 20, 0, 0);
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.setSize(size.width + radius, size.height + radius);
    return size;
  }

  private static class MouseListener extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
      NavButton button = (NavButton) e.getSource();
      button.isHovered = true;
      button.setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));

      if (!button.isActive) {
        button.setForeground(button.hoverTextColor);
      }
    }

    @Override
    public void mouseExited(MouseEvent e) {
      NavButton button = (NavButton) e.getSource();
      button.isHovered = false;
      button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

      if (!button.isActive) {
        button.setForeground(button.textColor);
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      NavButton button = (NavButton) e.getSource();
      button.parent.setNavButtonActive(button);
    }
  }
}
