package dev.components;

import dev.manager.FontManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LogoPanel extends JPanel {
  public LogoPanel() {
    setBackground(new Color(0, 0, 0, 0));
    setPreferredSize(new Dimension(300, 55));
    setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0xe4e4e7)));

    // Create the logo icon
    JLabel leftIcon = new JLabel(
        new ImageIcon(getIcon().getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
    leftIcon.setBounds(20, 60, 260, 260);
    add(leftIcon);

    // Create the logo text
    Font logoFont = FontManager.getFont("Inter", Font.BOLD, 24)
        .deriveFont(Map.of(TextAttribute.TRACKING, 0.08)); // Letter spacing
    JLabel logo = new JLabel("Spendwise");
    logo.setFont(logoFont);
    logo.setForeground(new Color(0x09090b));
    logo.setBounds(20, 20, 260, 30);
    add(logo);
  }

  private ImageIcon getIcon() {
    return new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
  }
}
