package dev.style;

import dev.manager.DatabaseManager;
import dev.manager.FontManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ModernScrollPane extends JScrollPane {
  private final DBTable table;
  private double[] columnWidths = new double[0];
  private ColumnFormatter columnFormatter;

  public ModernScrollPane(DBTable table) {
    super(table);
    this.table = table;

    setBorder(BorderFactory.createEmptyBorder());
    getViewport().setBackground(Color.WHITE);
    getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
    getVerticalScrollBar().setBackground(Color.WHITE);
    getVerticalScrollBar().setUI(new ModernScrollBarUI());

    table.setRowHeight(35);
    table.setShowVerticalLines(false);
    table.setColumnSelectionAllowed(false);
    table.setDefaultEditor(Object.class, null);
    table.setFont(FontManager.getFont("Inter", Font.PLAIN, 14));
    table.setBackground(Color.WHITE);
    table.setForeground(new Color(0x09090b));
    table.setGridColor(new Color(0xe4e4e7));
    table.setSelectionBackground(new Color(0xf3f4f6));
    table.setSelectionForeground(new Color(0x111827));
    table.getTableHeader().setReorderingAllowed(false);
    table.getTableHeader().setResizingAllowed(false);
    table.getTableHeader().setFont(FontManager.getFont("Inter", Font.BOLD, 14));
    table.getTableHeader().setPreferredSize(new Dimension(0, 40));
    table.setDefaultRenderer(Object.class, new VisitorRenderer());

    var renderer = new DefaultTableCellRenderer();
    renderer.setBorder(BorderFactory.createEmptyBorder());
    renderer.setForeground(new Color(0x71717a));
    renderer.setBackground(Color.WHITE);
    table.getTableHeader().setDefaultRenderer(renderer);

    table.getTableHeader().setUI(new BasicTableHeaderUI() {
      @Override
      public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        g.setColor(new Color(0xe4e4e7));
        g.fillRect(0, c.getHeight() - 1, c.getWidth(), 1);
      }
    });
  }

  public void setHeader(String[] headers) {
    var model = (DefaultTableModel) ((JTable) getViewport().getView()).getModel();
    model.setColumnIdentifiers(headers);
  }

  public void setColumnsWidth(double[] columnWidths) {
    this.columnWidths = columnWidths;
    for (int i = 0; i < columnWidths.length; i++) {
      table.getColumnModel().getColumn(i).setPreferredWidth((int) (getWidth() * columnWidths[i]));
    }
  }

  public void setColumnsFormat(ColumnFormatter formatter) {
    this.columnFormatter = formatter;
    var model = (DefaultTableModel) table.getModel();

    // Format each row in the table
    for (int i = 0; i < model.getRowCount(); i++) {
      for (int j = 0; j < model.getColumnCount(); j++) {
        model.setValueAt(formatter.format(model.getDataVector().get(i).toArray())[j], i, j);
      }
    }
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    setColumnsWidth(columnWidths);
  }

  private static class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
      this.thumbColor = new Color(0x6b7280);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
      return new JButton() {
        @Override
        public Dimension getPreferredSize() {
          return new Dimension(0, 0);
        }
      };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
      return new JButton() {
        @Override
        public Dimension getPreferredSize() {
          return new Dimension(0, 0);
        }
      };
    }
  }

  public static class VisitorRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
      return this;
    }
  }

  public interface ColumnFormatter {
    Object[] format(Object[] row);
  }

  public void refresh() {
    var model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);

    var data = DatabaseManager.queryAsTable(table.getQuery());
    assert data != null;

    for (int i = 0; i < data.getRowCount(); i++) {
      model.addRow(data.getRow(i));
    }

    setColumnsFormat(columnFormatter);
  }
}
