package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.RoundButton;
import dev.style.RoundComboBox;
import dev.style.RoundTextField;
import dev.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesModal extends JDialog {
    private final RoundTextField date, description, amount;
    private final RoundComboBox customer;

    private final Sales sales;

    public SalesModal(Sales sales, Object[] data) {
        this.sales = sales;

        setTitle(data == null ? "Nova Venda" : "Editar Venda");
        setSize(420, 430);
        setLocationRelativeTo(null);
        setLayout(null);
        setModal(true);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);

        var font = FontManager.getFont("Inter", Font.PLAIN, 14);
        String dateText = "", descriptionText = "", amountText = "", customerText = "";

        if (data != null) {
            dateText = data[1].toString();
            descriptionText = data[2].toString();
            amountText = data[3].toString().replaceAll("[^0-9.,-]", "");
            customerText = data[4].toString();
        }

        JLabel dateLabel = new JLabel("Data:");
        dateLabel.setFont(font);
        dateLabel.setBounds(20, 27, 360, 30);
        add(dateLabel);

        date = new RoundTextField(10, 10, 10);
        date.setBounds(20, 57, 360, 37);
        date.setFont(font);
        date.setBackground(Color.WHITE);
        date.setForeground(new Color(0x111827));
        date.setBorderColor(new Color(0xe5e5e8));
        date.setPlaceholder("Insira a data (dd/mm/yyyy)");
        date.setPlaceholderColor(new Color(0x6b7280));
        date.setText(dateText.isEmpty() ? Utilities.formatDate(new Date()) : dateText);
        add(date);

        JLabel descriptionLabel = new JLabel("Descrição:");
        descriptionLabel.setBounds(20, 97, 360, 30);
        descriptionLabel.setFont(font);
        add(descriptionLabel);

        description = new RoundTextField(10, 10, 10);
        description.setFont(font);
        description.setBackground(Color.WHITE);
        description.setForeground(new Color(0x111827));
        description.setBorderColor(new Color(0xe5e5e8));
        description.setBounds(20, 127, 360, 37);
        description.setPlaceholder("Insira a descrição");
        description.setPlaceholderColor(new Color(0x6b7280));
        description.setText(descriptionText);
        add(description);

        JLabel amountLabel = new JLabel("Valor:");
        amountLabel.setBounds(20, 167, 360, 30);
        amountLabel.setFont(font);
        add(amountLabel);

        amount = new RoundTextField(10, 10, 10);
        amount.setBounds(20, 197, 360, 37);
        amount.setFont(font);
        amount.setBackground(Color.WHITE);
        amount.setForeground(new Color(0x111827));
        amount.setBorderColor(new Color(0xe5e5e8));
        amount.setPlaceholder("Insira o valor (ex. 100.00)");
        amount.setPlaceholderColor(new Color(0x6b7280));
        amount.setText(amountText);
        add(amount);

        JLabel customerLabel = new JLabel("Cliente:");
        customerLabel.setBounds(20, 237, 360, 30);
        customerLabel.setFont(font);
        add(customerLabel);

        customer = getCustomerComboBox();
        customer.setForeground(new Color(0x111827));
        customer.setBackground(Color.WHITE);
        customer.setBorderColor(new Color(0xe5e5e8));
        customer.setBounds(20, 267, 360, 37);
        customer.setFont(font);
        customer.setSelectedItem(customerText);
        add(customer);

        RoundButton save = new RoundButton("Salvar", 10);
        save.setFont(font);
        save.setBounds(20, 330, 360, 40);
        save.addActionListener(e -> {
            var d = date.getText();
            var desc = description.getText();
            var val = Utilities.parseDouble(amount.getText());
            var customerName = customer.getSelectedItem().toString();
            var customerId = DatabaseManager.queryAsInt("SELECT id FROM customers WHERE name = ?", customerName);

            if (!Utilities.isValidDate(d)) {
                Utilities.showErrorMessage("Formato de data inválido.");
                return;
            }

            if (data == null) {
                DatabaseManager.update("INSERT INTO sales (date, description, amount, customer_id) VALUES (?, ?, ?, ?)", d, desc, val, customerId);
            } else {
                int id = Integer.parseInt(data[0].toString());
                DatabaseManager.update("UPDATE sales SET date = ?, description = ?, amount = ?, customer_id = ? WHERE id = ?", d, desc, val, customerId, id);
            }

            dispose();
            sales.refresh();
            Dashboard.getInstance().refresh();
        });
        add(save);

        setVisible(true);
    }

    private RoundComboBox getCustomerComboBox() {
        var customers = DatabaseManager.queryAsArray("SELECT DISTINCT name FROM customers ORDER BY name ASC");
        return new RoundComboBox(customers, 10, 10, 10);
    }
}
