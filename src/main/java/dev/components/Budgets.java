package dev.components;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Budgets extends JPanel {
  public Budgets() {
    setBackground(Color.WHITE);

    JLabel label = new JLabel("Budgets");
    label.setBounds(0, 0, 100, 100);
    add(label);
  }
}
