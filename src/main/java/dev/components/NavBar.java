package dev.components;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class NavBar extends JPanel {
  public NavBar() {
    setBounds(0, 0, 300, 720);
    setBackground(new Color(0xfafbfb));
    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));

    add(new Logo());
    add(new NavButton(this, "/img/dashboard.png", "Dashboard", new Dashboard()));
    add(new NavButton(this, "/img/transaction.png", "Transactions", new Transactions()));
    add(new NavButton(this, "/img/wallet.png", "Budgets", new Budgets()));
    add(new NavButton(this, "/img/target.png", "Goals", new Goals()));
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
