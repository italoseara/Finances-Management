package dev.style;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
}
