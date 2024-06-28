package dev.components;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Transactions extends JPanel {
  public Transactions() {
    setBackground(Color.WHITE);

    JLabel label = new JLabel("Transactions");
    label.setBounds(0, 0, 100, 100);
    add(label);
  }
}
