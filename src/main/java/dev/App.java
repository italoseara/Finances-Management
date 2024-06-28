package dev;

import dev.manager.FontManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
  private final FontManager fontManager = new FontManager();

  private final JPanel leftPanel;
  private final JPanel mainPanel;

  public App() {
    loadFonts();

    setTitle("Spendwise - Budget Manager");
    setIconImage(getIcon().getImage());

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1280, 720);
    setLocationRelativeTo(null);
    setLayout(null);

    // Create the left panel
    leftPanel = new JPanel();
    leftPanel.setBackground(new Color(0xfafbfb));
    leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));
    leftPanel.add(createLogo());
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

  private JPanel createLogo() {
    // Create the logo panel
    JPanel logoPanel = new JPanel();
    logoPanel.setBackground(new Color(0xfafbfb));
    logoPanel.setPreferredSize(new Dimension(300, 55));
    logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0xe4e4e7)));

    // Create the logo icon
    JLabel leftIcon = new JLabel(
        new ImageIcon(getIcon().getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
    leftIcon.setBounds(20, 60, 260, 260);
    logoPanel.add(leftIcon);

    // Create the logo text
    Font logoFont = fontManager.getFont("Inter", Font.BOLD, 24)
        .deriveFont(Map.of(TextAttribute.TRACKING, 0.08)); // Letter spacing
    JLabel logo = new JLabel("Spendwise");
    logo.setFont(logoFont);
    logo.setForeground(new Color(0x09090b));
    logo.setBounds(20, 20, 260, 30);
    logoPanel.add(logo);

    return logoPanel;
  }

  private ImageIcon getIcon() {
    return new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
  }

  private void loadFonts() {
    try {
      fontManager.loadFont("Inter", "Inter-Regular.ttf", Font.PLAIN);
      fontManager.loadFont("Inter", "Inter-Bold.ttf", Font.BOLD);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
