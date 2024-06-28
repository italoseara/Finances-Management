package dev.components;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Goals extends JPanel {
  public Goals() {
    setBackground(Color.WHITE);

    JLabel label = new JLabel("Goals");
    label.setBounds(0, 0, 100, 100);
    add(label);
  }
}
