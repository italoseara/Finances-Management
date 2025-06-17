package dev.components;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.RoundButton;
import dev.style.RoundComboBox;
import dev.style.RoundTextField;
import dev.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ExpensesModal extends JDialog {
    private final RoundTextField date, description, amount;
    private final RoundComboBox category;

    private final Expenses expenses;

    public ExpensesModal(Expenses expenses, Object[] data) {
        this.expenses = expenses;

        setTitle(data == null ? "Nova Despesa" : "Editar Despesa");
        setSize(420, 430);
        setLocationRelativeTo(null);
        setLayout(null);
        setModal(true);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);

        var font = FontManager.getFont("Inter", Font.PLAIN, 14);
        String dateText = "", descriptionText = "", amountText = "", categoryText = "";

        if (data != null) {
            dateText = data[1].toString();
            categoryText = data[2].toString();
            descriptionText = data[3].toString();
            amountText = data[4].toString().replaceAll("[^0-9.,-]", "");
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

        JLabel categoryLabel = new JLabel("Categoria:");
        categoryLabel.setBounds(20, 97, 360, 30);
        categoryLabel.setFont(font);
        add(categoryLabel);

        category = getCategoryComboBox();
        category.setForeground(new Color(0x111827));
        category.setBackground(Color.WHITE);
        category.setBorderColor(new Color(0xe5e5e8));
        category.setBounds(20, 127, 360, 37);
        category.setFont(font);
        category.setSelectedItem(categoryText);
        add(category);

        JLabel descriptionLabel = new JLabel("Descrição:");
        descriptionLabel.setBounds(20, 167, 360, 30);
        descriptionLabel.setFont(font);
        add(descriptionLabel);

        description = new RoundTextField(10, 10, 10);
        description.setFont(font);
        description.setBackground(Color.WHITE);
        description.setForeground(new Color(0x111827));
        description.setBorderColor(new Color(0xe5e5e8));
        description.setBounds(20, 197, 360, 37);
        description.setPlaceholder("Insira a descrição");
        description.setPlaceholderColor(new Color(0x6b7280));
        description.setText(descriptionText);
        add(description);

        JLabel amountLabel = new JLabel("Valor:");
        amountLabel.setBounds(20, 237, 360, 30);
        amountLabel.setFont(font);
        add(amountLabel);

        amount = new RoundTextField(10, 10, 10);
        amount.setBounds(20, 267, 360, 37);
        amount.setFont(font);
        amount.setBackground(Color.WHITE);
        amount.setForeground(new Color(0x111827));
        amount.setBorderColor(new Color(0xe5e5e8));
        amount.setPlaceholder("Insira o valor (ex. 150.00)");
        amount.setPlaceholderColor(new Color(0x6b7280));
        amount.setText(amountText);
        add(amount);

        RoundButton save = new RoundButton("Salvar", 10);
        save.setFont(font);
        save.setBounds(20, 330, 360, 40);
        save.addActionListener(e -> {
            var d = date.getText();
            var cat = category.getSelectedItem().toString();
            var desc = description.getText();
            var val = Utilities.parseDouble(amount.getText());

            if (!Utilities.isValidDate(d)) {
                Utilities.showErrorMessage("Formato de data inválido.");
                return;
            }

            if (data == null) {
                DatabaseManager.update("INSERT INTO expenses (date, category, description, amount) VALUES (?, ?, ?, ?)", d, cat, desc, val);
            } else {
                int id = Integer.parseInt(data[0].toString());
                DatabaseManager.update("UPDATE expenses SET date = ?, category = ?, description = ?, amount = ? WHERE id = ?", d, cat, desc, val, id);
            }

            dispose();
            expenses.refresh();
            Dashboard.getInstance().refresh();
        });
        add(save);

        setVisible(true);
    }

    private RoundComboBox getCategoryComboBox() {
        var categories = DatabaseManager.queryAsArray("SELECT DISTINCT category FROM expenses ORDER BY category ASC");
        return new RoundComboBox(categories, 10, 10, 10);
    }
}
