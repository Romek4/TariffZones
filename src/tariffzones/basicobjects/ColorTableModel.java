package tariffzones.basicobjects;

import java.awt.Color;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class ColorTableModel extends AbstractTableModel implements TableModelListener {
	//RowData rowData;
    private String[] columnNames = {};
	private Object[][] data = {};
	
	public ColorTableModel(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public int getColumnCount() {
	return columnNames.length;
	}
	
	public int getRowCount() {
	return data.length;
	}
	
	public String getColumnName(int col) {
	return columnNames[col];
	}
	
	public Object getValueAt(int row, int col) {
//		System.out.println(data[row][col].toString());
		return data[row][col];
	}
	
	public Object[][] getData() {
		return data;
	}
	
	/*
	* JTable uses this method to determine the default renderer/
	* editor for each cell.  If we didn't implement this method,
	* then the last column would contain text ("true"/"false"),
	* rather than a check box.
	*/
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	public boolean isCellEditable(int row, int col) {
	//Note that the data/cell address is constant,
	//no matter where the cell appears onscreen.
		if (col < 1) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public void setData(Object[][] data) {
		this.data = data;
		fireTableDataChanged();
	}

	@Override
	public void tableChanged(TableModelEvent e) {
//		System.out.println(getRowCount());
	}
}

