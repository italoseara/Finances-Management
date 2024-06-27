package dev;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;
import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
  private final JPanel leftPanel;
  private final JPanel mainPanel;

  public App() {
    setTitle("Budget Manager");
    setIconImage(getIcon());

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1280, 720);
    setLocationRelativeTo(null);
    setLayout(null);

    // Create the left panel
    leftPanel = new JPanel();
    leftPanel.setBackground(new Color(0xfafbfb));
    leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));
    add(leftPanel);

    // Create the main panel
    mainPanel = new JPanel();
    mainPanel.setBackground(Color.WHITE);
    add(mainPanel);

    // Use a component listener to handle resizing
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        int width = getWidth();
        int height = getHeight();

        // 40% of the width or 300px, whichever is smaller
        int leftWidth = Math.min((int) (width * 0.4f), 300);
        int rightWidth = width - leftWidth; // The remaining width

        leftPanel.setBounds(0, 0, leftWidth, height);
        mainPanel.setBounds(leftWidth, 0, rightWidth, height);
      }
    });

    setVisible(true);
  }

  private Image getIcon() {
    return new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png"))).getImage();
  }
}
