package dev.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

public class NavBar extends JPanel {
  public NavBar() {
    setBounds(0, 0, 300, 720);
    setLayout(new FlowLayout());
    setBackground(new Color(0xfafbfb));
    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));

    add(Box.createVerticalStrut(80));
    add(new Logo());
    add(new NavButton(this, "/img/dashboard.png", "Dashboard", Dashboard.class, true));
    add(new NavButton(this, "/img/transaction.png", "Transactions", Transactions.class));
    add(new NavButton(this, "/img/wallet.png", "Budgets", Budgets.class));
    add(new NavButton(this, "/img/target.png", "Goals", Goals.class));
  }

  public void setNavButtonActive(NavButton navButton) {
    for (Component component : getComponents()) {
      if (component instanceof NavButton button) {
        button.setActive(false);
      }
    }

    navButton.setActive(true);
  }
}
