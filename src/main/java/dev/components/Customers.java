package dev.components;


import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import dev.style.DBTable;
import dev.style.ModernScrollPane;
import dev.style.RoundButton;
import dev.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;


public class Customers extends JPanel {
    private final JLabel title;
    private final ModernScrollPane scrollPane;

    private final RoundButton addButton;
    private final RoundButton updateButton;
    private final RoundButton removeButton;

    public Customers() {
        setLayout(null);
        setBackground(Color.WHITE);

        int entries = DatabaseManager.queryAsInt("SELECT COUNT(*) FROM customers;");
        title = new JLabel("Clientes (%d)".formatted(entries));
        title.setFont(FontManager.getFont("Inter", Font.BOLD, 24).deriveFont(Map.of(TextAttribute.TRACKING, 0.04)));
        title.setBounds(20, 20, 500, 30);
        title.setForeground(new Color(0x111827));
        add(title);

        addButton = new RoundButton("Novo Cliente", 10);
        addButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(new Color(0x111827));
        addButton.setHoverColor(new Color(0xf8f4f4));
        addButton.setBorderColor(new Color(0xe5e5e8));
        addButton.setBounds(0, 20, 135, 35);
        addButton.addActionListener(e -> new CustomersModal(this, null));
        add(addButton);

        updateButton = new RoundButton("Editar Selecionado", 10);
        updateButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
        updateButton.setBackground(Color.WHITE);
        updateButton.setForeground(new Color(0x111827));
        updateButton.setHoverColor(new Color(0xf8f4f4));
        updateButton.setBorderColor(new Color(0xe5e5e8));
        updateButton.setBounds(0, 20, 185, 35);
        updateButton.addActionListener(e -> onUpdate());
        add(updateButton);

        removeButton = new RoundButton("Deletar Selecionado", 10);
        removeButton.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
        removeButton.setBackground(Color.WHITE);
        removeButton.setForeground(new Color(0x111827));
        removeButton.setHoverColor(new Color(0xf8f4f4));
        removeButton.setBorderColor(new Color(0xe5e5e8));
        removeButton.setBounds(0, 20, 185, 35);
        removeButton.addActionListener(e -> onRemove());
        add(removeButton);

        var table = DatabaseManager.queryAsTable("""
                    SELECT id, name, email, phone FROM customers
                    ORDER BY name DESC;
                """);

        scrollPane = new ModernScrollPane(table);
        scrollPane.setHeader(new String[]{"ID", "Nome", "E-mail", "Telefone"});
        scrollPane.setColumnsWidth(new double[]{.07, .23, .35, .35});

        scrollPane.setBounds(20, 70, 760, 680);
        add(scrollPane);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        scrollPane.setBounds(20, 70, width - 40, height - 120);
        addButton.setBounds(width - 160, 20, 135, 35);
        updateButton.setBounds(width - 350, 20, 185, 35);
        removeButton.setBounds(width - 540, 20, 185, 35);
    }

    private void onUpdate() {
        var table = scrollPane.getTable();
        var selectedRows = table.getSelectedRows();
        if (selectedRows.length == 1) {
            Object[] data = table.getRow(selectedRows[0]);
            new CustomersModal(this, data);
        }
    }

    private void onRemove() {
        DBTable table = scrollPane.getTable();
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            return;
        }

        String[] ids = new String[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            ids[i] = String.valueOf(table.getValueAt(selectedRows[i], 0));
        }

        DatabaseManager.update("""
                    DELETE FROM customers WHERE id IN (%s);
                """.formatted(String.join(", ", ids)));
        refresh();
    }

    public void refresh() {
        int entries = DatabaseManager.queryAsInt("SELECT COUNT(*) FROM customers;");
        title.setText("Clientes (%d)".formatted(entries));
        scrollPane.refresh();
    }
}
