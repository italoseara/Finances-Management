package dev.components;

import java.awt.*;
import javax.swing.*;

public class NavBar extends JPanel {
    public NavBar() {
        setBounds(0, 0, 300, 720);
        setBackground(new Color(0xfafbfb));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe4e4e7)));

        add(new Logo());
        add(new NavButton(this, "/img/dashboard.png", "Dashboard", new Dashboard()));
        add(new NavButton(this, "/img/sales.png", "Vendas", new Sales()));
        add(new NavButton(this, "/img/wallet.png", "Despesas", new Expenses()));
        add(new NavButton(this, "/img/customer.png", "Clientes", new Customers()));
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
