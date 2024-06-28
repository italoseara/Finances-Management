package dev.components;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

public class LeftPanel extends JPanel {

  public LeftPanel() {
    setBounds(0, 0, 300, 720);
    setLayout(new FlowLayout());
    setBackground(new Color(0xfafbfb));
    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));

    add(Box.createVerticalStrut(70));
    add(new LogoPanel());
    add(new NavButton("/img/dashboard.png", "Dashboard", true));
    add(new NavButton("/img/transaction.png", "Transactions"));
    add(new NavButton("/img/wallet.png", "Budgets"));
    add(new NavButton("/img/target.png", "Goals"));
  }
}
