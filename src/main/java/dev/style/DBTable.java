package dev.style;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DBTable extends JTable {
  private final String query;

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
  public void addColumn(ColumnAdder formatter) {
    TableColumn column = new TableColumn();
    addColumn(column);

    var model = (DefaultTableModel) getModel();
    for (int i = 0; i < getRowCount(); i++) {
      Object[] row = getRow(i);
      Object value = formatter.format(row);

      Vector<Object> rowData = model.getDataVector().elementAt(i);
      rowData.add(value);
    }
  }

  public interface ColumnAdder {
    Object format(Object[] row);
  }
}
