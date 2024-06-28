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

public class Logo extends JPanel {
  public Logo() {
    setBackground(new Color(0, 0, 0, 0));
    setPreferredSize(new Dimension(300, 55));
    setMaximumSize(new Dimension(310, 55));
    setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0xe4e4e7)));

    // Create the logo icon
    JLabel leftIcon = new JLabel(getIcon());
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
    ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/logo.png")));
    return new ImageIcon(icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
  }
}
