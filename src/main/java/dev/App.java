package dev;

import dev.components.NavBar;
import dev.components.NavButton;
import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.util.Utilities;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;
import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
  private static App instance;

  private final NavBar navBar = new NavBar();
  private JPanel content = new JPanel();

  public App() {
    // Load custom fonts
    loadFonts();

    // Initialize the app window
    ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/logo.png")));
    setTitle("Spendwise - Budget Manager");
    setIconImage(icon.getImage());

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 720);
    setMinimumSize(new Dimension(800, 500));
    setLocationRelativeTo(null);
    setLayout(null);

    // Add components to the app window
    add(navBar);
    add(content);

    addComponentListener(new AppListener());
    setVisible(true);
  }

  private void loadFonts() {
    try {
      FontManager.loadFont("Inter", "font/Inter-Regular.ttf", Font.PLAIN);
      FontManager.loadFont("Inter", "font/Inter-Bold.ttf", Font.BOLD);
    } catch (Exception e) {
      Utilities.showErrorMessage(e.getMessage());
    }
  }

  public void setContent(JPanel panel) {
    remove(content);
    panel.setBounds(content.getX(), content.getY(), content.getWidth(), content.getHeight());
    content = panel;
    add(content);
    revalidate();
    repaint();
  }

  public static App getInstance() {
    return instance;
  }

  private class AppListener extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
      int width = getWidth();
      int height = getHeight();

      int leftWidth = 300; // 300px
      int rightWidth = width - leftWidth; // The remaining width

      navBar.setBounds(0, 0, leftWidth, height);
      content.setBounds(leftWidth, 0, rightWidth, height);
    }
  }

  public static void main(String[] args) {
    // Connect to the SQLite database
    DatabaseManager.connect("database.db");

    // Create an instance of the app
    instance = new App();

    // Set the dashboard as the default content
    ((NavButton) instance.navBar.getComponent(1)).setActive(true);
  }
}
