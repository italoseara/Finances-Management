package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.RoundButton;
import dev.style.RoundTextField;

import javax.swing.*;
import java.awt.*;

public class CustomersModal extends JDialog {
    private final RoundTextField name, email, phone;

    private final Customers customers;

    public CustomersModal(Customers customers, Object[] data) {
        this.customers = customers;

        setTitle(data == null ? "Novo Cliente" : "Editar Cliente");
        setSize(420, 350);
        setLocationRelativeTo(null);
        setLayout(null);
        setModal(true);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);

        var font = FontManager.getFont("Inter", Font.PLAIN, 14);

        String nameText = "", emailText = "", phoneText = "";

        if (data != null) {
            nameText = data[1].toString();
            emailText = data[2] != null ? data[2].toString() : "";
            phoneText = data[3] != null ? data[3].toString() : "";
        }

        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setBounds(20, 27, 360, 30);
        nameLabel.setFont(font);
        add(nameLabel);

        name = new RoundTextField(10, 10, 10);
        name.setBounds(20, 57, 360, 37);
        name.setFont(font);
        name.setBackground(Color.WHITE);
        name.setForeground(new Color(0x111827));
        name.setBorderColor(new Color(0xe5e5e8));
        name.setPlaceholder("Insira o nome do cliente");
        name.setPlaceholderColor(new Color(0x6b7280));
        name.setText(nameText);
        add(name);

        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setBounds(20, 97, 360, 30);
        emailLabel.setFont(font);
        add(emailLabel);

        email = new RoundTextField(10, 10, 10);
        email.setBounds(20, 127, 360, 37);
        email.setFont(font);
        email.setBackground(Color.WHITE);
        email.setForeground(new Color(0x111827));
        email.setBorderColor(new Color(0xe5e5e8));
        email.setPlaceholder("Insira o e-mail (Opcional)");
        email.setPlaceholderColor(new Color(0x6b7280));
        email.setText(emailText);
        add(email);

        JLabel phoneLabel = new JLabel("Telefone:");
        phoneLabel.setBounds(20, 167, 360, 30);
        phoneLabel.setFont(font);
        add(phoneLabel);

        phone = new RoundTextField(10, 10, 10);
        phone.setBounds(20, 197, 360, 37);
        phone.setFont(font);
        phone.setBackground(Color.WHITE);
        phone.setForeground(new Color(0x111827));
        phone.setBorderColor(new Color(0xe5e5e8));
        phone.setPlaceholder("Insira o número de telefone (Opcional)");
        phone.setPlaceholderColor(new Color(0x6b7280));
        phone.setText(phoneText);
        add(phone);

        RoundButton save = new RoundButton("Salvar", 10);
        save.setFont(font);
        save.setBounds(20, 260, 360, 40);
        save.addActionListener(e -> {
            var n = name.getText();
            var em = email.getText();
            var ph = phone.getText();

            if (data == null) {
                DatabaseManager.update("INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)", n, em, ph);
            } else {
                int id = Integer.parseInt(data[0].toString());
                DatabaseManager.update("UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?", n, em, ph, id);
            }

            dispose();
            this.customers.refresh();
            Dashboard.getInstance().refresh();
        });
        add(save);

        setVisible(true);
    }
}