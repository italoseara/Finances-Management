package dev.components;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class LeftPanel extends JPanel {
  private final JPanel logoPanel;

  public LeftPanel() {
    setBackground(new Color(0xfafbfb));
    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));

    // Create the logo panel
    logoPanel = new LogoPanel();
    add(logoPanel);
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    logoPanel.setBounds(0, 5, logoPanel.getWidth(), logoPanel.getHeight());
  }
}
