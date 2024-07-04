package dev.style;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DBTable extends JTable {
  private final String query;
  private ColumnAdder columnAdder;

  public DBTable(DefaultTableModel model, String query) {
    super(model);
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public Object[] getRow(int i) {
    var model = (DefaultTableModel) getModel();
    return model.getDataVector().elementAt(i).toArray();
  }

  @SuppressWarnings("unchecked")
  public void addColumn(ColumnAdder columnAdder) {
    this.columnAdder = columnAdder;
    TableColumn column = new TableColumn();
    addColumn(column);

    var model = (DefaultTableModel) getModel();
    for (int i = 0; i < getRowCount(); i++) {
      Object[] row = getRow(i);
      Object value = columnAdder.format(row);

      Vector<Object> rowData = model.getDataVector().elementAt(i);
      rowData.add(value);
    }
  }

  public ColumnAdder getColumnAdder() {
    return columnAdder;
  }

  public interface ColumnAdder {
    Object format(Object[] row);
  }
}
