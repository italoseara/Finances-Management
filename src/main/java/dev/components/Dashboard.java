package dev.components;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dashboard extends JPanel {
  public Dashboard() {
    setBackground(Color.WHITE);

    JLabel label = new JLabel("Dashboard");
    label.setBounds(0, 0, 100, 100);
    add(label);
  }
}
